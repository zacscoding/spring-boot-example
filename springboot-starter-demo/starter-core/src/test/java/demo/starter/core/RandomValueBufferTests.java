package demo.starter.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient.Builder;

/**
 *
 */
public class RandomValueBufferTests {

    @Test
    public void testPublishWithMaxSize() throws Exception {
        // given
        final int maxSize = 5;
        final Duration maxTime = Duration.ofSeconds(1L);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final RandomValueListener listener = values -> {
            countDownLatch.countDown();
            assertThat(values.size()).isEqualTo(maxSize);
        };

        final RandomValueBuffer buffer = new RandomValueBuffer(listener);
        buffer.start(maxSize, maxTime);

        for (int i = 0; i < maxSize; i++) {
            buffer.publishRandomId(UUID.randomUUID().toString());
        }

        // when then
        assertThat(countDownLatch.await(500L, TimeUnit.MILLISECONDS)).isTrue();
    }

    @Test
    public void testPublishWithMaxTime() throws Exception {
        int maxSize = 5;
        Duration maxTime = Duration.ofMillis(500L);

        final CountDownLatch countDownLatch = new CountDownLatch(1);
        RandomValueListener listener = values -> {
            countDownLatch.countDown();
        };

        RandomValueBuffer buffer = new RandomValueBuffer(listener);
        buffer.start(maxSize, maxTime);

        for (int i = 0; i < 2; i++) {
            buffer.publishRandomId(UUID.randomUUID().toString());
        }

        assertThat(countDownLatch.await(600L, TimeUnit.MILLISECONDS)).isTrue();
    }
}
