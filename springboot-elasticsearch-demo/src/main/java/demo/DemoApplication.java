package demo;

import java.util.Map.Entry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@SpringBootApplication
@RestController
public class DemoApplication {

    private static ConfigurableApplicationContext ctx;

    public static void main(String[] args) throws Exception {
        ctx = SpringApplication.run(DemoApplication.class, args);
        afterContextLoaded();
    }

    private static void afterContextLoaded() throws Exception {
        for (Entry<String, ContextListener> entry : ctx.getBeansOfType(ContextListener.class).entrySet()) {
            entry.getValue().afterContextLoaded();
        }
    }

}