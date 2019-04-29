package demo;

import java.util.Arrays;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@Slf4j
@SpringBootApplication
public class DemoApplication {

    @Autowired
    Environment env;

    @PostConstruct
    private void setUp() {
        System.out.println("Active profile :: " + Arrays.toString(env.getActiveProfiles()));
    }

    public static void main(String[] args) {
        String activeProfile = "dev";
        System.setProperty("spring.profiles.active", activeProfile);
        SpringApplication.run(DemoApplication.class, args);
        logger.trace("this is trace..");
        logger.debug("this is debug..");
        logger.info("this is info..");
        logger.warn("this is warn..");
        logger.error("this is error");
    }
}
