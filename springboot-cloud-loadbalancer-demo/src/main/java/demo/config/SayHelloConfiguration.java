package demo.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * https://spring.io/guides/gs/spring-cloud-loadbalancer/
 */
@Profile("client")
@Configuration
@Slf4j
public class SayHelloConfiguration {

    @Bean
    @Primary
    ServiceInstanceListSupplier serviceInstanceListSupplier() {
        return new DemoServiceInstanceListSuppler("say-hello");
    }

    public static class DemoServiceInstanceListSuppler implements ServiceInstanceListSupplier {

        private final String serviceId;

        DemoServiceInstanceListSuppler(String serviceId) {
            this.serviceId = serviceId;
        }

        @Override
        public String getServiceId() {
            logger.info("DemoServiceInstanceListSuppler::getServiceId() is called");
            return serviceId;
        }

        @Override
        public Flux<List<ServiceInstance>> get() {
            //final String host = "192.168.79.130";
            final String host = "localhost";
            logger.info("DemoServiceInstanceListSuppler::get() is called");

            final List<ServiceInstance> servers = Arrays.asList(
                    new DefaultServiceInstance(serviceId + "1", serviceId, host, 3000, false)
                    , new DefaultServiceInstance(serviceId + "2", serviceId, host, 3001, false)
                    , new DefaultServiceInstance(serviceId + "3", serviceId, host, 3002, false)
            );

            return Flux.just(servers);
        }
    }
}
