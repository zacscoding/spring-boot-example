package demo;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j(topic = "[MAIN]")
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(DemoApplication.class, args);
        // IntStream.range(0, 30).forEach(i -> System.out.println());

        log.info("## Active profiles : {}"
            , Arrays.toString(ctx.getEnvironment().getActiveProfiles())
        );
    }
}
