package demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@Slf4j
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping(value = "/sleep/{time}")
    public ResponseEntity<Void> sleep(@PathVariable("time") long sleep) {
        try {
            log.info("## Request sleep {} [ms]", sleep);
            Thread.sleep(sleep);
            return ResponseEntity.ok().build();
        } catch (InterruptedException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
