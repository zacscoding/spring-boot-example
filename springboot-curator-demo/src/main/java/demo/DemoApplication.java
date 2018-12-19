package demo;

import java.util.Arrays;
<<<<<<< HEAD
import java.util.stream.IntStream;
=======
>>>>>>> 002563b7770a84f6688053d5255f03b09ac31adf
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j(topic = "[MAIN]")
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(DemoApplication.class, args);
<<<<<<< HEAD
        IntStream.range(0, 30).forEach(i -> System.out.println());
=======
        // IntStream.range(0, 30).forEach(i -> System.out.println());
>>>>>>> 002563b7770a84f6688053d5255f03b09ac31adf

        log.info("## Active profiles : {}"
            , Arrays.toString(ctx.getEnvironment().getActiveProfiles())
        );
    }
}
