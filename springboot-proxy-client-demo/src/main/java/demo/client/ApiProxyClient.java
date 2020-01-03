package demo.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @GitHub : https://github.com/zacscoding
 */
public class ApiProxyClient {

    private AtomicLong indexGenerator = new AtomicLong(-1L);

    private final List<String> targetUrls;
    private final List<HttpHost> targetHosts;
    private RestTemplate restTemplate;

    public ApiProxyClient(List<String> targetUrls) {
        this.targetUrls = targetUrls;
        this.targetHosts = new ArrayList<>(targetUrls.size());
        initialize();
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    private void initialize() {
        for (String targetUrl : targetUrls) {
            targetHosts.add(new HttpHost(targetUrl));
        }

        final HttpHost proxy = new HttpHost("proxy.example.com");

        HttpClient httpClient = HttpClientBuilder.create().setRoutePlanner(new DefaultProxyRoutePlanner(proxy) {

            @Override
            public HttpHost determineProxy(HttpHost target, HttpRequest request, HttpContext context)
                    throws HttpException {

                int idx = (int) indexGenerator.getAndIncrement() % targetUrls.size();

                return targetHosts.get(idx);
            }
        }).build();

        restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
    }
}
