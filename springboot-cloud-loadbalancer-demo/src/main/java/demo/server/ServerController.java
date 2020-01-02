package demo.server;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * https://spring.io/guides/gs/spring-cloud-loadbalancer/
 */
@RestController
@Slf4j
public class ServerController {

    @GetMapping("/greeting")
    public String greet() {
        logger.info("Access /greeting");

        List<String> greetings = Arrays.asList("Hi there", "Greetings", "Salutations");
        Random rand = new Random();

        return greetings.get(rand.nextInt(greetings.size()));
    }

    @GetMapping("/")
    public String home() {
        logger.info("Access /");
        return "Hi!";
    }
}
