package demo;

import java.util.Arrays;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        logger.info("Start a server with {}", Arrays.toString(args));
        SpringApplication.run(DemoApplication.class, args);
    }

    @Autowired
    Environment environment;

    @GetMapping("/data")
    public ResponseEntity<String> getData() {
        final String port = environment.getProperty("local.server.port");
        logger.info("Server http://localhost:{}/data is called", port);
        return ResponseEntity.ok(UUID.randomUUID().toString());
    }

    @GetMapping("/alive")
    public ResponseEntity<Void> getAlive() {
        return ResponseEntity.ok().build();
    }
}
