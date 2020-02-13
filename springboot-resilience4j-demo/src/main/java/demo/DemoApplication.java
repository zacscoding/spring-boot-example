package demo;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DemoApplication {

    private AtomicInteger counter = new AtomicInteger(1);

    @GetMapping("/hello")
    public String sayHello() throws Exception {

        if (counter.getAndIncrement() % 7 == 1) {
            return "Hello :)";
        }

        TimeUnit.SECONDS.sleep(5000L);

        return "Hello :)";
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
