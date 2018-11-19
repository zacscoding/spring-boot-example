package demo.client;

import java.lang.invoke.SerializedLambda;
import java.util.concurrent.TimeUnit;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 * @author zacconding
 * @Date 2018-11-19
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RestTemplateTimeoutTest {

    @LocalServerPort
    private int port;

    private RestTemplate restTemplate;

    @Before
    public void setUp() {
        restTemplate = new RestTemplate(getClientHttpRequestFactory());
    }

    @Test
    public void testTimeout() {
        System.out.println("## local random port : " + port);
        Thread timeCheck = new Thread(() -> {
            try {
                int second = 0;
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println(second + " sec ...");
                    TimeUnit.SECONDS.sleep(1L);
                    second++;
                }
            } catch (InterruptedException e) {
            }
        });
        timeCheck.setDaemon(true);
        timeCheck.start();

        // String url = "http://localhost:1989" + port + "/sleep/5000";
        String url = "http://localhost:1989/sleep/5000";
        try {
            restTemplate.getForEntity(url, null, Void.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        timeCheck.interrupt();
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        int timeout = 5000;
        RequestConfig config = RequestConfig.custom()
                                            .setConnectTimeout(1000) // time until establish conn
                                            .setConnectionRequestTimeout(timeout)
                                            .setSocketTimeout(timeout) // data read
                                            .build();

        CloseableHttpClient client = HttpClientBuilder
            .create()
            .setDefaultRequestConfig(config)
            .build();

        return new HttpComponentsClientHttpRequestFactory(client);
    }
}