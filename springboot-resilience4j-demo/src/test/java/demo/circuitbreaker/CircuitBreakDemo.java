package demo.circuitbreaker;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.ImmutableMap;

import demo.LogLevelUtil;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.State;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.CheckedFunction0;

public class CircuitBreakDemo {

    private static final String DEMO1_NAME = "demo1";

    private RestTemplate restTemplate;
    private Map<String, String> endpoints;

    @BeforeEach
    public void setUp() {
        LogLevelUtil.setInfo("io.github.resilience4j");
        LogLevelUtil.setOff();
        HttpComponentsClientHttpRequestFactory httpRequestFactory =
                new HttpComponentsClientHttpRequestFactory();

        httpRequestFactory.setConnectionRequestTimeout(3000);
        httpRequestFactory.setConnectTimeout(3000);
        httpRequestFactory.setReadTimeout(3000);

        restTemplate = new RestTemplate(httpRequestFactory);
        endpoints = ImmutableMap.of(DEMO1_NAME, "http://localhost:3000/hello");
    }

    @Test
    public void runTests() throws Exception {
        final CircuitBreakerConfig circuitBreakerConfig =
                CircuitBreakerConfig.custom()
                                    .failureRateThreshold(50)
                                    .slidingWindowType(SlidingWindowType.COUNT_BASED)
                                    .waitDurationInOpenState(Duration.ofMillis(5000))
                                    .permittedNumberOfCallsInHalfOpenState(2)
                                    .slidingWindowSize(2)
                                    .recordExceptions(RestClientException.class, ResourceAccessException.class)
                                    .build();

        final CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);
        final CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(DEMO1_NAME);

        final CheckedFunction0<String> decoratedSupplier = CircuitBreaker
                .decorateCheckedSupplier(circuitBreaker, () -> {
                    String endpoint = endpoints.get(DEMO1_NAME);
                    System.out.println("# Try to request " + DEMO1_NAME + " to " + endpoint);
                    System.out.flush();
                    try {
                        return restTemplate.getForObject(endpoint, String.class);
                    } catch (Exception e) {
                        throw e;
                    }
                });

        for (int i = 0; i < 100; i++) {
            State state = circuitBreaker.getState();
            boolean success = false;
            String message = null;
            long start = System.currentTimeMillis();
            try {
                message = circuitBreaker
                        .executeSupplier(() -> {
                            String endpoint = endpoints.get(DEMO1_NAME);
                            System.out.println("Try to request " + DEMO1_NAME + " to " + endpoint);
                            System.out.flush();
                            try {
                                return restTemplate.getForObject(endpoint, String.class);
                            } catch (Exception e) {
                                throw e;
                            }
                        });
                success = true;
            } catch (Exception e) {
                message = e.getMessage();
            }
            long elapsed = System.currentTimeMillis() - start;
            System.out.printf("> [#%d] state : %s, result : %s. reason : %s. [%d ms]\n", i, state,
                              success, message, elapsed);
            System.out.flush();

            TimeUnit.SECONDS.sleep(1L);
        }
    }
}
