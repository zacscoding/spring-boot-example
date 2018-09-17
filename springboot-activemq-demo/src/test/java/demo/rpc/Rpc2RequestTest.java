package demo.rpc;

import demo.rpc2.Rpc2Client;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zacconding
 * @Date 2018-09-17
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Rpc2RequestTest {

    private AtomicInteger failure = new AtomicInteger(0);

    @Autowired
    private Rpc2Client rpc2Client;

    @Test
    public void basic() {
        final int count = 10;
        IntStream.range(0, count).forEach(i -> {
            String request = UUID.randomUUID().toString();
            String response = rpc2Client.processRequest(request);
            System.out.println("## Req : " + request + " -> " + response);
        });
    }

    @Test
    public void multiple() throws InterruptedException {
        final int threadCount = 1000;
        Thread[] t = new Thread[threadCount];

        long start = System.currentTimeMillis();
        for (int i = 0; i < threadCount; i++) {
            t[i] = new Thread(() -> {
                String request = UUID.randomUUID().toString();
                String response = rpc2Client.processRequest(request);
                if (!request.toUpperCase().equals(response)) {
                    failure.incrementAndGet();
                }
            });
            t[i].setDaemon(true);
            t[i].start();
        }

        for (int i = 0; i < threadCount; i++) {
            t[i].join();
        }
        long elapsed = System.currentTimeMillis() - start;

        System.out.println("Try :: " + threadCount + " > Failure : " + failure.get() + " " + elapsed + "[MS]");
    }
}
