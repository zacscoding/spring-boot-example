package demo.config;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Profile("server")
@ComponentScan("demo.server")
@Slf4j
public class ServerConfiguration {

    @PostConstruct
    private void setUp() {
        logger.info("## Enable server");
    }
}
