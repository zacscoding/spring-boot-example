package demo.client;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

/**
 * @author zacconding
 * @Date 2018-12-06
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RestTemplateTimeoutTest {

    @LocalServerPort
    private int port;

    @Test
    public void checkConnectTimeout() {
        // requestTimeout :
        // connectTimeout : 처음 connect 할때 시간?
        // readTimeout    :
        RestTemplate template = createRestTemplate(1000, 10000, 1000);

        long start = System.currentTimeMillis();
        log.info(">> start");
        try {
            String url = "http://localhost:" + (port) + "/okhttp/delay/2000";
            ResponseEntity<String> entity = template.getForEntity(url, String.class);
            System.out.println(entity.getStatusCode());
        } catch(Exception e) {
            log.warn(e.getMessage());
        } finally {
            long elapsed = System.currentTimeMillis() - start;
            log.info(">> elapsed : {}", elapsed);
        }
    }


    private RestTemplate createRestTemplate(int requestTimeout, int connectTimeout, int readTimeout) {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();

        httpRequestFactory.setConnectionRequestTimeout(requestTimeout);
        httpRequestFactory.setConnectTimeout(connectTimeout);
        httpRequestFactory.setReadTimeout(readTimeout);

        return new RestTemplate(httpRequestFactory);
    }
}
