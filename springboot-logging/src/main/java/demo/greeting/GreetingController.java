package demo.greeting;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class GreetingController {

    private final GreetingService greetingService;

    @GetMapping("/hello/{name}")
    public ResponseEntity<String> handleHello(@PathVariable("name") String name) {
        logger.info("[GreetingController] handleHello() is called. name: {}", name);

        if (name.length() < 3) {
            logger.error("[GreetingController] name length must be greater than 3");
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(greetingService.sayHello(name));
    }
}
