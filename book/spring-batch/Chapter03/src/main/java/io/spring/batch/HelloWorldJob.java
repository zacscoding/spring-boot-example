package io.spring.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class HelloWorldJob {

    public static void main(String[] args) {
        // (1) Tasklet 기반 예제
        // System.setProperty("spring.profiles.active", "tasklet-example,repeatable");
        // System.setProperty("spring.profiles.active", "tasklet-example,stack-trace-logging");
        //System.setProperty("spring.profiles.active", "tasklet-example,callable-adapter");

        // (2) Chunk-oriented 예제
//        System.setProperty("spring.profiles.active", "chunk-example-basic");
//        args = addAll(args, "inputFile=resources/data/input.txt", "outputFile=./resources/data/output.txt");
        // System.setProperty("spring.profiles.active", "chunk-example-static");

        // (3) Flow 예제
        // System.setProperty("spring.profiles.active", "conditional-job-example");
        System.setProperty("spring.profiles.active", "programmatic-job-example");

        checkActiveProfiles();
        final ConfigurableApplicationContext ctx = SpringApplication.run(HelloWorldJob.class, args);
        displayBeans(ctx);
    }

    private static void checkActiveProfiles() {
        final String defaultProfile = "job-example";
        final String profileValues = System.getProperty("spring.profiles.active");

        if (StringUtils.hasText(profileValues)) {
            return;
        }

        logger.info("Sets to default profile because empty active profile. default: {}", defaultProfile);
        System.setProperty("spring.profiles.active", defaultProfile);
    }

    private static String[] addAll(String[] source, String... values) {
        if (source == null || source.length == 0) {
            return values;
        }

        String[] dest = new String[source.length + values.length];
        System.arraycopy(source, 0, dest, 0, source.length);
        System.arraycopy(values, 0, dest, source.length, values.length);

        return dest;
    }

    private static void displayBeans(ConfigurableApplicationContext ctx) {
        for (String beanName : ctx.getBeanDefinitionNames()) {
            if (!isLoggingBean(beanName)) {
                continue;
            }
            logger.info("## Bean name: {} > {}", beanName, ctx.getBean(beanName).getClass().getSimpleName());
        }
    }

    private static boolean isLoggingBean(String beanName) {
        final String[] packages = {
                "io.spring.batch",
                "org.springframework.batch"
        };

        for (String p : packages) {
            if (beanName.startsWith(p)) {
                return true;
            }
        }

        return false;
    }
}
