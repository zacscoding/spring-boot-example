package demo;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

class DemoApplicationTests {

    @Test
    public void callServer() throws Exception {
        final int repeat = 10;
        final CountDownLatch countDownLatch = new CountDownLatch(repeat);

        for (int i = 0; i < repeat; i++) {
            new Thread(() -> {
                try {
                    new RestTemplate().getForEntity("http://localhost:8080/hello/{name}", String.class,
                                                    UUID.randomUUID().toString());
                } finally {
                    countDownLatch.countDown();
                }
            }).start();
        }
        countDownLatch.await();
    }
}
