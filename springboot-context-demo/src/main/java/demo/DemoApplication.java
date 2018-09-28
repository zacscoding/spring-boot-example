package demo;

import java.util.PriorityQueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(DemoApplication.class, args);
        System.out.println(">>>> Complete to load");
        String displayBeanNames = ctx.getEnvironment().getProperty("display.beans.enabled", "false");
        if ("true".equalsIgnoreCase(displayBeanNames)) {
            displayBeanNames(ctx);
        }
        ctx.close();
    }

    private static void displayBeanNames(ConfigurableApplicationContext ctx) {
        // TEMP CODE
        System.out.println(">> Check bean names");
        String[] beanNames = ctx.getBeanDefinitionNames();
        PriorityQueue<String> queue = new PriorityQueue<>();
        for (String beanName : beanNames) {
            //System.out.println(beanName);
            queue.offer(beanName);
        }

        while (!queue.isEmpty()) {
            System.out.println(queue.poll());
        }
        // -- TEMP CODE
    }
}
