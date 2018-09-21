package demo.condition;

import demo.condition.TypeEnabledCondition.TypeAEnabledCondition;
import demo.condition.TypeEnabledCondition.TypeBEnabledCondition;
import demo.condition.TypeEnabledCondition.TypeCEnabledCondition;
import javax.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author zacconding
 * @Date 2018-09-21
 * @GitHub : https://github.com/zacscoding
 */
public class ConditionConfiguration {

    @Configuration
    @ConditionalOnProperty(name = "condition.test.enabled", havingValue = "true")
    @Conditional(value = TypeAEnabledCondition.class)
    public static class TypeAConfiguration {

        @PostConstruct
        private void setUp() {
            System.out.println("## TypeAConfiguration() setup is called..");
        }

        @Bean(name = "typeName")
        public String typeName() {
            return "typeA";
        }
    }

    @Configuration
    @ConditionalOnProperty(name = "condition.test.enabled", havingValue = "true")
    @Conditional(value = TypeBEnabledCondition.class)
    public static class TypeBConfiguration {

        @PostConstruct
        private void setUp() {
            System.out.println("## TypeBConfiguration() setup is called..");
        }

        @Bean(name = "typeName")
        public String typeName() {
            return "typeB";
        }

    }

    @Configuration
    @ConditionalOnProperty(name = "condition.test.enabled", havingValue = "false")
    @Conditional(value = TypeCEnabledCondition.class)
    public static class TypeCConfiguration {

        @PostConstruct
        private void setUp() {
            System.out.println("## TypeCConfiguration() setup is called..");
        }

        @Bean(name = "typeName")
        public String typeName() {
            return "typeC";
        }
    }
}
