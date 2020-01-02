package demo.config;

import javax.annotation.PostConstruct;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Configuration
@Profile("client")
@ComponentScan("demo.client")
@LoadBalancerClient(name = "say-hello", configuration = SayHelloConfiguration.class)
@Slf4j
public class ClientConfiguration {

    @PostConstruct
    private void setUp() {
        logger.info("## Enable client");
    }
}
