package demo.starter.server;

import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.starter.core.RandomValueBuffer;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Configuration
@RestController
@Slf4j
public class StarterApplication {

    private final RandomValueBuffer buffer;

    public StarterApplication(RandomValueBuffer buffer) {this.buffer = buffer;}

    public static void main(String[] args) {
        SpringApplication.run(StarterApplication.class, args);
    }

    @GetMapping("/push")
    public String pushValue(@RequestParam(value = "repeat", defaultValue = "1") int repeat) {
        for (int i = 0; i < repeat; i++) {
            buffer.publishRandomId(UUID.randomUUID().toString());
        }

        return "Success to push UUID " + repeat + " times";
    }

}
