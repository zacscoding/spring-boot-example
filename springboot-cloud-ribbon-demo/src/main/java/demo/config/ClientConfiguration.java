package demo.config;

import javax.annotation.PostConstruct;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Profile("client")
@ComponentScan("demo.client")
@RibbonClient(name = "say-hello", configuration = SayHelloConfiguration.class)
@Slf4j
public class ClientConfiguration {

    @PostConstruct
    private void setUp() {
        logger.info("## Enable client");
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
