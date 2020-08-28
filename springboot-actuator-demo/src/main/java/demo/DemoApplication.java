package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        final ConfigurableApplicationContext ctx = SpringApplication.run(DemoApplication.class, args);
        //displayBeans(ctx);
    }

    static void displayBeans(ConfigurableApplicationContext ctx) {
        final StringBuilder sb = new StringBuilder();

        for (String beanName : ctx.getBeanDefinitionNames()) {
            final Object bean = ctx.getBean(beanName);
            if (bean.getClass().getName().startsWith("org.springframework.boot.actuate")) {
                sb.append(bean.getClass().getName()).append(" - ").append(beanName).append('\n');
            }
        }
        logger.info("## Display beans \n{}", sb.toString());
    }
}
