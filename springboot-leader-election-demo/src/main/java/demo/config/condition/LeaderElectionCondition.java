package demo.config.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import demo.constants.AppConstants.LeaderElectionConstants;

/**
 * Condition of leadership
 */
public class LeaderElectionCondition {

    public static abstract class AbstractClusterCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return getRequiredClusterType().equals(
                    context.getEnvironment().getProperty("cluster.leadership.type"));
        }

        abstract String getRequiredClusterType();
    }

    /**
     * Condition of zookeeper
     */
    public static class ZookeeperLeaderElectionCondition extends AbstractClusterCondition {

        @Override
        String getRequiredClusterType() {
            return LeaderElectionConstants.TYPE_ZOOKEEPER;
        }
    }

    public static class RedisLeaderElectionCondition extends AbstractClusterCondition {

        @Override
        String getRequiredClusterType() {
            return LeaderElectionConstants.TYPE_REDIS;
        }
    }
}
