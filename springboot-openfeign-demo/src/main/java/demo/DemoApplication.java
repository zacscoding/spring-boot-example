package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

import com.netflix.config.ConfigurationManager;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication(
        exclude = {
                RibbonAutoConfiguration.class
        }
)
@ComponentScan(
        excludeFilters = {
                @Filter(type = FilterType.REGEX, pattern = "demo.lowerservice\\..*")
                , @Filter(type = FilterType.REGEX, pattern = "demo.upperservice\\..*")
                , @Filter(type = FilterType.REGEX, pattern = "demo.client\\..*")
                , @Filter(type = FilterType.REGEX, pattern = "demo.willremoved\\..*")
        }
)
@Slf4j
public class DemoApplication {

    public static void main(String[] args) throws Exception {
        ConfigurationManager.loadPropertiesFromResources("default.properties");
        final ConfigurableApplicationContext ctx = SpringApplication.run(DemoApplication.class, args);
        displayBeans(ctx);
    }

    private static void displayBeans(ConfigurableApplicationContext ctx) {
        final String[] beanNames = ctx.getBeanDefinitionNames();
        final StringBuilder builder = new StringBuilder();

        for (String beanName : beanNames) {
            final Object beanObj = ctx.getBean(beanName);

            if (isDisplayBean(beanObj.getClass().getName())) {
                builder.append("- ").append(beanName).append("\n    : ")
                       .append(beanObj.getClass().getName()).append('\n');
            }
        }

        logger.info("\n// ============= display beans =============\n{}"
                    + "================================================== //", builder);
    }

    private static boolean isDisplayBean(String className) {
        final String[] startsWiths = {
                "org.springframework.cloud",
                "com.netflix",
                "feign"
        };

        for (String startsWith : startsWiths) {
            if (className.startsWith(startsWith)) {
                return true;
            }
        }

        return false;
    }
}
