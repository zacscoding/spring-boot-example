package demo.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author zacconding
 * @Date 2018-09-21
 * @GitHub : https://github.com/zacscoding
 */
public class TypeEnabledCondition {

    public static class TypeAEnabledCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            System.out.println("## TypeAEnabledCondition::matches is called");
            String type = context.getEnvironment().getProperty("condition.test.type");
            return "typeA".equals(type);
        }
    }

    public static class TypeBEnabledCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            System.out.println("## TypeBEnabledCondition::matches is called");
            String type = context.getEnvironment().getProperty("condition.test.type");
            return "typeB".equals(type);
        }
    }

    public static class TypeCEnabledCondition implements Condition {

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            System.out.println("## TypeCEnabledCondition::matches is called");
            String type = context.getEnvironment().getProperty("condition.test.type");
            return "typeC".equals(type);
        }
    }
}