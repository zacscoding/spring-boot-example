package demo;

import static org.junit.Assert.fail;

import javax.net.ssl.SSLContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SSLClientTests {

    @LocalServerPort
    private int port;

    @Test(expected = ResourceAccessException.class)
    public void requestWithNoSSLThenThrowEx() {
        new RestTemplate().getForObject("https://127.0.0.1:" + port, String.class);
        fail();
    }

    @Test
    public void request() throws Exception {
    }
}
