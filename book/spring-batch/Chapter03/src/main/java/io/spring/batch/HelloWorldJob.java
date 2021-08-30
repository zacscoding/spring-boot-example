package io.spring.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class HelloWorldJob {

    public static void main(String[] args) {
        checkActiveProfiles();
        SpringApplication.run(HelloWorldJob.class, args);
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
}
