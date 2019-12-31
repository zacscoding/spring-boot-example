package demo.dev;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demo.common.LogLevelUtil;
import demo.leader.LeaderElection;
import demo.leader.zookeeper.ZookeeperLeaderElection;

public class ZookeeperLeaderElectionTest {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperLeaderElectionTest.class);

    @BeforeEach
    public void setUp() {
        LogLevelUtil.setWarn();
        LogLevelUtil.setTrace("demo");
    }

    @Test
    public void runTests() throws Exception {
        int count = 3;
        String lockName = "leader-lock";
        String connectString = "192.168.79.130:2181";

        List<CuratorFramework> curatorFrameworks = new ArrayList<>();
        List<LeaderElection> leaderElections = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            final String threadName = "thread-" + i;

            RetryPolicy retryPolicy = new RetryNTimes(
                    3,
                    1000);

            CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
            LeaderElection leaderElection =
                    new ZookeeperLeaderElection(curatorFramework, lockName,
                                                () -> logger.info("onTakeLeadership.. {}", threadName));

            curatorFramework.start();
            leaderElection.start();

            leaderElections.add(leaderElection);
            curatorFrameworks.add(curatorFramework);
        }

        TimeUnit.SECONDS.sleep(5L);

        for (int i = 0; i < count; i++) {
            Iterator<LeaderElection> iterator = leaderElections.iterator();
            while (iterator.hasNext()) {
                LeaderElection leaderElection = iterator.next();

                if (leaderElection.isLeader()) {
                    leaderElection.shutdown();
                    iterator.remove();
                }
            }

            if (i != count - 1) {
                TimeUnit.SECONDS.sleep(5L);
            }
        }

        for (CuratorFramework curator : curatorFrameworks) {
            curator.close();
        }
    }

    private static class LeaderElectionContext {

        private LeaderElection leaderElection;
        private CuratorFramework curatorFramework;
    }
}
