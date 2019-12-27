package demo.starter.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import demo.starter.core.RandomValueListener;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
@Configuration
public class ServerConfiguration {

    @Bean
    public RandomValueListener randomValueListener() {
        logger.info("will create a RandomValueListener bean in server.");
        return values -> logger.info("onNewValues with size : {}. consumed from server", values.size());
    }
}
