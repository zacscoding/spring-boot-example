package io.spring.batch.stepexample;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomService {

    public void service() {
        logger.info("called service() method");
    }

    public void service(String message) {
        logger.info("called service(String) method. message: {}", message);
    }
}
