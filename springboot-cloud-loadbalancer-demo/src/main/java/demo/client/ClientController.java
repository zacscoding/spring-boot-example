package demo.client;

import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * https://spring.io/guides/gs/spring-cloud-loadbalancer/
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class ClientController {

    private final WebClient.Builder loadBalancedWebClientBuilder;
    private final ReactorLoadBalancerExchangeFilterFunction lbFunction;

    @GetMapping("/hi")
    public Mono<String> hi(@RequestParam(value = "name", defaultValue = "Mary") String name) {
        return loadBalancedWebClientBuilder.build()
                                           .get()
                                           .uri("http://say-hello/greeting")
                                           .retrieve()
                                           .bodyToMono(String.class)
                                           .map(greeting -> String.format("%s, %s!", greeting, name));
    }

    @GetMapping("/hello")
    public Mono<String> hello(@RequestParam(value = "name", defaultValue = "John") String name) {
        return WebClient.builder()
                        .filter(lbFunction)
                        .build()
                        .get()
                        .uri("http://say-hello/greeting")
                        .retrieve()
                        .bodyToMono(String.class)
                        .map(greeting -> String.format("%s, %s!", greeting, name));
    }
}
