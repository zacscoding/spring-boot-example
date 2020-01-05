package demo.config;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceConfiguration {

    @Profile("lower-service")
    @Configuration
    @ComponentScan("demo.lowerservice")
    @Slf4j
    public static class LowerServiceConfiguration {

        @PostConstruct
        private void setUp() {
            logger.info("## Enable lower service");
        }
    }

    @Profile("upper-service")
    @Configuration
    @ComponentScan("demo.upperservice")
    @Slf4j
    public static class UpperServiceConfiguration {

        @PostConstruct
        private void setUp() {
            logger.info("## Enable upper service");
        }
    }
}
