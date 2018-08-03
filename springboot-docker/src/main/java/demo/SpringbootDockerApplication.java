package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * https://spring.io/guides/gs/spring-boot-docker/
 */
@SpringBootApplication
@RestController
public class SpringbootDockerApplication {

    @GetMapping("/")
    public String index() {
        return "Welcome docker demo~!";
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringbootDockerApplication.class, args);
    }
}
