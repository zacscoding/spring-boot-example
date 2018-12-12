package demo.curator;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import demo.util.LogLevelUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.async.AsyncCuratorFramework;
import org.apache.curator.x.async.modeled.JacksonModelSerializer;
import org.apache.curator.x.async.modeled.ModelSpec;
import org.apache.curator.x.async.modeled.ModeledFramework;
import org.apache.curator.x.async.modeled.ZPath;
import org.junit.Test;

/**
 * https://www.baeldung.com/apache-curator
 */
public class CuratorBasicUsageTest {

    @Test
    public void usage_retryPolicies() throws Exception {
        CuratorFramework client = newClient();
        client.start();

        assertNotNull(client.checkExists().forPath("/"));
        System.out.println("client :: " + client.toString());

        client.close();
    }

    @Test
    public void usage_retryPoliciesAsync() throws InterruptedException {
        CuratorFramework client = newClient();
        client.start();

        AsyncCuratorFramework async = AsyncCuratorFramework.wrap(client);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicBoolean exist = new AtomicBoolean(false);
        async.checkExists().forPath("/").thenAcceptAsync(e -> {
            exist.set(e != null);
            countDownLatch.countDown();
        });

        countDownLatch.await();

        assertTrue(exist.get());
    }

    @Test
    public void usage_configManagement() throws Exception {
        CuratorFramework clent = newClient();
        clent.start();

        AsyncCuratorFramework async = AsyncCuratorFramework.wrap(clent);
        String key = "/my_key";
        String expected = "my_value";

        clent.create().forPath(key);

        async.setData().forPath(key, expected.getBytes());

        CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicBoolean isEquals = new AtomicBoolean();
        async.getData().forPath(key).thenAccept(data -> {
            isEquals.set(new String(data).equals(expected));
            countDownLatch.countDown();
        });

        countDownLatch.await();
        assertTrue(isEquals.get());
    }

    @Test
    public void usage_watchers() throws InterruptedException {
        CuratorFramework client = newClient();
        client.start();

        AsyncCuratorFramework async = AsyncCuratorFramework.wrap(client);
        String key = "/my_key";
        String expected = "my_value";

        async.create().forPath(key);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        List<String> changes = new ArrayList<>();
        async.watched().getData().forPath(key).event()
             .thenAccept(watchedEvent -> {
                 try {
                     changes.add(new String(client.getData().forPath(watchedEvent.getPath())));
                     countDownLatch.countDown();
                 } catch(Exception e) {
                     e.printStackTrace();
                 }
             });

        async.setData().forPath(key, expected.getBytes());
        countDownLatch.await();

        assertThat(changes.get(0), is(expected));
    }

    @Test
    public void usage_typedModel() throws Exception {
        ModelSpec<HostConfig> hostConfigSpec = ModelSpec.builder(
                ZPath.parseWithIds("/config"),
                JacksonModelSerializer.build(HostConfig.class)
        ).build();

        CuratorFramework client = newClient();
        client.start();

        AsyncCuratorFramework async = AsyncCuratorFramework.wrap(client);
        ModeledFramework<HostConfig> modeledClient = ModeledFramework.wrap(async, hostConfigSpec);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        modeledClient.set(new HostConfig("host-name", 8080));
        modeledClient.read()
                     .whenComplete((value, error) -> {
                         if (error != null) {
                             error.printStackTrace();
                             fail("Cannot read host config");
                         } else {
                             assertThat(value.getHostName(), is("host-name"));
                             assertTrue(value.port == 8080);
                         }
                         countDownLatch.countDown();
                     });

        countDownLatch.await();
    }

    @Test
    public void usage_leaderElection() throws Exception {
        int task = 5;

        LeaderElectionTask[] threads = new LeaderElectionTask[task];
        IntStream.range(0, 5).forEach(i -> {
            threads[i] = new LeaderElectionTask();
            threads[i].setDaemon(true);
            threads[i].start();
        });

        TimeUnit.MINUTES.sleep(5L);

        IntStream.range(0, 5).forEach(i -> {
            threads[i].close();
        });

        /* output
        ## take leader ship :: Curator-LeaderSelector-2 : org.apache.curator.framework.imps.CuratorFrameworkImpl@127dd3dc
        ## Will release lock :: Curator-LeaderSelector-2

        ## take leader ship :: Curator-LeaderSelector-0 : org.apache.curator.framework.imps.CuratorFrameworkImpl@7f045f4b
        ## Will release lock :: Curator-LeaderSelector-0

        ## take leader ship :: Curator-LeaderSelector-3 : org.apache.curator.framework.imps.CuratorFrameworkImpl@3d43db2e
        ## Will release lock :: Curator-LeaderSelector-3

        ## take leader ship :: Curator-LeaderSelector-1 : org.apache.curator.framework.imps.CuratorFrameworkImpl@6dd58d9c
        ## Will release lock :: Curator-LeaderSelector-1

        ## take leader ship :: Curator-LeaderSelector-4 : org.apache.curator.framework.imps.CuratorFrameworkImpl@5bc97599
        ## Will release lock :: Curator-LeaderSelector-4
         */
    }

    @Test
    public void usage_sharedLock() throws Exception {
        LogLevelUtil.setOff();
        int taskCount = 5;

        SharedLockTask[] tasks = new SharedLockTask[taskCount];
        for (int i = 0; i < taskCount; i++) {
            tasks[i] = new SharedLockTask("Task-" + i);
            tasks[i].start();
        }

        for (int i = 0; i < taskCount; i++) {
            tasks[i].join();
        }
        /* output
        ## Acquire lock : Task-2
        ## Will release : Task-2
        ## Acquire lock : Task-3
        working... Task-3 - 0
        working... Task-3 - 1
        working... Task-3 - 2
        ## Will release : Task-3
        ## Acquire lock : Task-0
        ## Will release : Task-0
        ## Acquire lock : Task-4
        ## Will release : Task-4
        ## Acquire lock : Task-1
        working... Task-1 - 0
        working... Task-1 - 1
        working... Task-1 - 2
        ## Will release : Task-1
         */
    }

    @Test
    public void usage_counter() throws Exception {
        LogLevelUtil.setOff();

        final String sharedCountPath = "/counters/A";
        CuratorFramework client = newClient();
        client.start();
        if (client.checkExists().forPath(sharedCountPath) != null) {
            client.delete().forPath(sharedCountPath);
        }

        int taskCount = 10;
        Thread[] threads = new Thread[taskCount];

        IntStream.range(0, taskCount).forEach(i -> {
            threads[i] = new Thread(() -> {
                CuratorFramework curatorClient = newClient();
                try {
                    curatorClient.start();

                    SharedCount counter = new SharedCount(curatorClient, sharedCountPath, 1);
                    counter.start();
                    while (!counter.trySetCount(counter.getCount() + 1)) {
                        //System.out.println(">> Retry : " + i);
                    }
                    System.out.println("Success " + i);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    curatorClient.close();
                }
            });

            threads[i].setDaemon(true);
            threads[i].start();
        });

        for (int i = 0; i < taskCount; i++) {
            threads[i].join();
        }

        SharedCount counter = new SharedCount(client, sharedCountPath, 1);
        counter.start();
        System.out.println("## >> Result : " + counter.getCount());

        /*
        Success 5
        Success 6
        Success 8
        Success 0
        Success 4
        Success 2
        Success 7
        Success 9
        Success 3
        Success 1
        ## >> Result : 11
         */
    }

    private static CuratorFramework newClient() {
        int sleepMsBetweenRetries = 100;
        int maxRetries = 3;
        RetryPolicy retryPolicy = new RetryNTimes(maxRetries, sleepMsBetweenRetries);

        return CuratorFrameworkFactory.newClient("192.168.5.78:2181", retryPolicy);
    }

    private static final class SharedLockTask extends Thread {
        CuratorFramework client;

        String name;

        public SharedLockTask(String name) {
            this.name = name;
        }


        @Override
        public void run() {
            client = newClient();
            client.start();
            InterProcessSemaphoreMutex sharedLock = new InterProcessSemaphoreMutex(client, "/mutex/process/A");

            try {
                sharedLock.acquire();

                System.out.println("## Acquire lock : " + name);
                int repeat = new Random().nextInt(5);
                for (int i = 0; i < repeat; i++) {
                    System.out.printf("working... %s - %d\n", name, i);
                    TimeUnit.SECONDS.sleep(1L);
                }

                System.out.println("## Will release : " + name);
                sharedLock.release();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                client.close();
            }
        }
    }

    private static final class LeaderElectionTask extends Thread {

        private LeaderSelector leaderSelector;
        private CuratorFramework client;

        @Override
        public void run() {
            try {
                int sleepMsBetweenRetries = 100;
                int maxRetries = 3;
                RetryPolicy retryPolicy = new RetryNTimes(maxRetries, sleepMsBetweenRetries);

                client = CuratorFrameworkFactory.newClient("192.168.5.78:2181", retryPolicy);
                client.start();
                System.out.println("## " + Thread.currentThread().getName() + " :: " + client.toString());

                leaderSelector = new LeaderSelector(client, "/mutex/select/leader/for/job/A", new LeaderSelectorListener() {
                    @Override
                    public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
                        System.out.println("## take leader ship :: " + Thread.currentThread().getName() + " : " + curatorFramework.toString());
                        TimeUnit.MILLISECONDS.sleep(new Random().nextInt(1000));
                        System.out.println("## Will release lock :: " + Thread.currentThread().getName());
                    }

                    @Override
                    public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
                        System.out.println("## stateChanged :: " + Thread.currentThread().getName() + " : " + curatorFramework.toString() + " : " + connectionState.toString());
                    }
                });

                // join the members group
                leaderSelector.start();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        public void close() {
            leaderSelector.close();
        }
    }

    private static final class HostConfig {

        private String hostName;
        private int port;

        public HostConfig() {

        }

        public HostConfig(String hostName, int port) {
            this.hostName = hostName;
            this.port = port;
        }

        public String getHostName() {
            return hostName;
        }

        public void setHostName(String hostName) {
            this.hostName = hostName;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }
}
