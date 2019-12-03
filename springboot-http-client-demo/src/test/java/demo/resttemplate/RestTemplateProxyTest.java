package demo.resttemplate;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.protocol.HttpContext;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @GitHub : https://github.com/zacscoding
 */
public class RestTemplateProxyTest {

    private AtomicInteger atomicInteger = new AtomicInteger(0);
    private CloseableHttpClient httpClient = HttpClients.createDefault();

    private HttpHost[] hosts = {
            new HttpHost("localhost", 8081, "http"),
            new HttpHost("localhost", 8082, "http"),
            new HttpHost("localhost", 8083, "http")
    };

    @Test
    public void runTests() throws Exception {
        RestTemplate restTemplate = build();

        for (int i = 0; i < 20; i++) {
            ResponseEntity<String> result = restTemplate.getForEntity("http://proxy.example.com/data",
                                                                      String.class);
            System.out.println(result.getBody());

            TimeUnit.SECONDS.sleep(1L);
        }
    }

    // create a new RestTemplate instance with proxy configuration
    private RestTemplate build() {
        final HttpHost proxy = new HttpHost("proxy.example.com");
        final HttpClient httpClient
                = HttpClientBuilder.create()
                                   .setRoutePlanner(new DefaultProxyRoutePlanner(proxy) {

                                       @Override
                                       public HttpHost determineProxy(HttpHost target,
                                                                      HttpRequest request,
                                                                      HttpContext context) {

                                           return getActiveHost().orElse(hosts[0]);
                                       }
                                   }).build();

        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
    }

    // returns active http host
    private Optional<HttpHost> getActiveHost() {
        final int start = atomicInteger.getAndIncrement();

        for (int i = 0; i < hosts.length; i++) {
            final int idx = (start + i) % hosts.length;
            final HttpHost targetHost = hosts[idx];

            CloseableHttpResponse response = null;
            try {
                HttpGet request = new HttpGet(targetHost.toURI().concat("/alive"));

                response = httpClient.execute(request);

                if (response.getStatusLine().getStatusCode() == 200) {
                    return Optional.of(targetHost);
                }
            } catch (IOException e) {
                // ignore
            } finally {
                if (response != null) {
                    try {
                        response.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return Optional.empty();
    }
}
