package demo.dev;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import demo.common.LogLevelUtil;
import demo.leader.LeaderElection;
import demo.leader.redis.RedisLeaderElection;

public class RedissonLeaderElectionTest {

    @BeforeEach
    public void setUp() {
        LogLevelUtil.setWarn();
        LogLevelUtil.setTrace("demo");
    }

    @Test
    public void runTests() throws Exception {
        int count = 3;
        String lockName = "leader-lock";
        String redisAddr = "redis://192.168.79.130:6379";

        List<RedissonClient> redissonClients = new ArrayList<>();
        List<LeaderElection> leaderElections = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            final String threadName = "thread-" + i;
            Config config = new Config();
            config.useSingleServer().setAddress(redisAddr);

            RedissonClient redissonClient = Redisson.create(config);
            RedisLeaderElection leaderElection =
                    new RedisLeaderElection(redissonClient, lockName,
                                            () -> System.out.println(">> onTakeLeadership : " + threadName));

            leaderElection.start();

            redissonClients.add(redissonClient);
            leaderElections.add(leaderElection);
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

        for (RedissonClient redissonClient : redissonClients) {
            redissonClient.shutdown();
        }
    }
}
