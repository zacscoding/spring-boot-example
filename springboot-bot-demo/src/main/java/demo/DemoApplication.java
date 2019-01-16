package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

@SpringBootApplication(scanBasePackages = {"me.ramswaroop.jbot", "demo"})
public class DemoApplication {

    public static void main(String[] args) throws Exception {
        beforeApplication();
        SpringApplication.run(DemoApplication.class, args);
    }

    private static void beforeApplication() throws Exception {
        ClassPathResource resource = new ClassPathResource("secret.yaml");
        if (resource.exists()) {
            System.setProperty("spring.config.location", resource.getFile().getAbsolutePath());
        }
    }
}

