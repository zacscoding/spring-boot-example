package demo;

import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@SpringBootApplication
public class ProxyClientDemoApplication {

    @GetMapping("/alive")
    public ResponseEntity<Void> getAlive() {
        logger.info("/alive is called");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/data")
    public ResponseEntity<String> getDate() {
        logger.info("/data is called");
        return ResponseEntity.ok(UUID.randomUUID().toString());
    }

    public static void main(String[] args) {
        SpringApplication.run(ProxyClientDemoApplication.class, args);
    }

}
