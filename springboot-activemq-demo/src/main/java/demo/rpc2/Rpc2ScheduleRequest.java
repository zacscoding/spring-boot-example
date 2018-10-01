package demo.rpc2;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-09-17
 * @GitHub : https://github.com/zacscoding
 */
@Profile("rpc2")
@ConditionalOnProperty(name = "rpc2.request.schedule", havingValue = "true")
@Slf4j
@Component
public class Rpc2ScheduleRequest {

    private AtomicInteger failure = new AtomicInteger(0);

    @Autowired
    private Rpc2Client rpc2Client;

    @Scheduled(fixedRate = 5000L, initialDelay = 1000L)
    public void generate() throws Exception {
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
