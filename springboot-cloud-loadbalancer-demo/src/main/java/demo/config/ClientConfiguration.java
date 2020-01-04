package demo.config;

import javax.annotation.PostConstruct;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Configuration
@Profile("client")
@ComponentScan("demo.client")
@LoadBalancerClient(name = "say-hello", configuration = SayHelloConfiguration.class)
@EnableDiscoveryClient
@RequiredArgsConstructor
@Slf4j
public class ClientConfiguration {

    private final LoadBalancerClientFactory loadBalancerClientFactory;
    private final ReactiveLoadBalancer.Factory loadBalancerFactory;
    private final Environment environment;

    @PostConstruct
    private void setUp() {
        logger.info("## Enable client");
        logger.info(">> Check loadbalancer factory : {}-{}", loadBalancerFactory.getClass().getName(),
                    loadBalancerFactory);

        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        System.out.println("## " + LoadBalancerClientFactory.PROPERTY_NAME + " ==> " + name);
    }

    @LoadBalanced
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
