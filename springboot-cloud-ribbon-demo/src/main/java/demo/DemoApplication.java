package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@ComponentScan(
        excludeFilters = {
                @Filter(type = FilterType.REGEX, pattern = "demo.server\\..*")
                , @Filter(type = FilterType.REGEX, pattern = "demo.client\\..*")
        }
)
@Slf4j
public class DemoApplication {

    public static void main(String[] args) {
        final ConfigurableApplicationContext ctx = SpringApplication.run(DemoApplication.class, args);
        displayBeans(ctx);
    }

    private static void displayBeans(ConfigurableApplicationContext ctx) {
        final String[] beanNames = ctx.getBeanDefinitionNames();
        final StringBuilder builder = new StringBuilder();

        for (String beanName : beanNames) {
            final Object beanObj = ctx.getBean(beanName);

            if (isDisplayBean(beanObj.getClass().getName())) {
                builder.append('[').append(beanName).append("] : ")
                       .append(beanObj.getClass().getName()).append('\n');
            }
        }

        logger.info("\n// ============= display beans =============\n{}"
                    + "================================================== //", builder);
    }

    private static boolean isDisplayBean(String className) {
        String[] startsWiths = {
                "com.netflix.ribbon",
                "org.springframework.cloud.netflix.ribbon"
        };

        for (String startsWith : startsWiths) {
            if (className.startsWith(startsWith)) {
                return true;
            }
        }

        return false;
    }
}
