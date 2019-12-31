package demo.config;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import demo.config.condition.LeaderElectionCondition.RedisLeaderElectionCondition;
import demo.config.condition.LeaderElectionCondition.ZookeeperLeaderElectionCondition;
import demo.config.properties.LeaderElectionProperties;
import demo.config.properties.LeaderElectionProperties.Redis;
import demo.config.properties.LeaderElectionProperties.Zookeeper;
import demo.constants.AppConstants.LeaderElectionConstants;
import demo.leader.LeaderElection;
import demo.leader.LeaderElectionListener;
import demo.leader.redis.RedisLeaderElection;
import demo.leader.zookeeper.ZookeeperLeaderElection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@ConditionalOnProperty(value = "cluster.leadership.enabled", havingValue = "true")
@EnableConfigurationProperties({ LeaderElectionProperties.class })
@Slf4j
@RequiredArgsConstructor
public class LeaderElectionConfiguration {

    private final Environment environment;
    private final LeaderElectionProperties properties;

    @PostConstruct
    public void setUp() {
        List<String> leaderElectionTypes = Arrays.asList(
                LeaderElectionConstants.TYPE_ZOOKEEPER
                , LeaderElectionConstants.TYPE_REDIS
        );

        final String clusterType = environment.getProperty("cluster.leadership.type");

        if (leaderElectionTypes.indexOf(clusterType) < 0) {
            throw new UnsupportedOperationException("Not supported leader election type " + clusterType);
        }
    }

    @Bean
    public LeaderElectionListener listener() {
        return () -> logger.info("## onTaskLeadership from {}", properties.getId());
    }

    @Configuration
    @Conditional(value = ZookeeperLeaderElectionCondition.class)
    @RequiredArgsConstructor
    public static class ZookeeperConfiguration {

        private final LeaderElectionProperties properties;
        private final LeaderElectionListener listener;

        @Bean(destroyMethod = "shutdown")
        public LeaderElection leaderElection(CuratorFramework curatorFramework) {
            return new ZookeeperLeaderElection(curatorFramework, properties.getLeaderShip().getLockName(),
                                               listener);
        }

        @Bean
        public CuratorFramework curatorFramework() {
            Zookeeper zookeeper = properties.getLeaderShip().getZookeeper();

            RetryPolicy retryPolicy = new RetryNTimes(
                    zookeeper.getMaxRetries(),
                    zookeeper.getSleepMsBetweenRetries());

            CuratorFramework client = CuratorFrameworkFactory.newClient(zookeeper.getConnectString(),
                                                                        retryPolicy);

            client.start();

            return client;
        }
    }

    @Configuration
    @Conditional(value = RedisLeaderElectionCondition.class)
    @RequiredArgsConstructor
    public static class RedisConfiguration {

        private final LeaderElectionProperties properties;
        private final LeaderElectionListener listener;

        @Bean(destroyMethod = "shutdown")
        public LeaderElection leaderElection() {
            return new RedisLeaderElection(redissonClient(),
                                           properties.getLeaderShip().getLockName(),
                                           listener);
        }

        //@Bean(destroyMethod = "shutdown")
        public RedissonClient redissonClient() {
            Redis redisProperties = properties.getLeaderShip().getRedis();
            String[] nodes = redisProperties.getAddress().split(",");

            if (nodes.length == 0) {
                throw new RuntimeException("Invalid redis address : " + redisProperties.getAddress());
            }

            Config redissonConfig = new Config();

            if (nodes.length == 1) {
                redissonConfig.useSingleServer().setAddress(nodes[0]);
            } else {
                redissonConfig.useClusterServers()
                              .addNodeAddress(nodes);
            }

            return Redisson.create(redissonConfig);
        }
    }
}
