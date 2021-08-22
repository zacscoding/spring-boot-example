package demo.greeting;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GreetingService {

    public String sayHello(String name) {
        logger.info("[GreetingService] sayHello() called. name: {}", name);
        return String.format("Hello %s :)", name);
    }
}
