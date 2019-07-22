package demo.config.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Enable database type
 */
public class DatabaseEnabledCondition {

    /**
     * Condition of leveldb
     */
    public static class LevelDbEnabledCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata annotatedTypeMetadata) {
            return "leveldb".equals(context.getEnvironment().getProperty("db.type"));
        }
    }

}
