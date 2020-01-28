package demo.collector;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 */
public class AccessLogCollectorExample {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void runTests() throws Exception {
        final AccessLogCollector collector = new AccessLogCollector();
        collector.start();

        int nthread = 10;
        Thread[] threads = new Thread[nthread];

        for (int i = 0; i < nthread; i++) {
            threads[i] = new Thread(() -> {
                try {
                    for (int j = 0; j < 10; j++) {
                        String accessLog = createAccessLog();
                        if (accessLog == null) {
                            j--;
                            continue;
                        }

                        collector.pushAccessLog(accessLog);

                        TimeUnit.MILLISECONDS.sleep(new Random().nextInt(3000));
                    }
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            });

            threads[i].setDaemon(true);
            threads[i].start();
        }

        for (int i = 0; i < nthread; i++) {
            threads[i].join();
        }

        System.out.println(">> Requested count : " + collector.getRequestedCount());

    }

    private String createAccessLog() {
        String[] requestUsers = {
                "zac", "amelia", "hazel", "lily", "sophia", "hyang", "lucas", "zoe"
        };
        String[] methods = {
                "GET", "PUT", "PATCH", "POST", "DELETE"
        };

        Map<String, Object> map = new HashMap<>();
        map.put("@timestamp", LocalDateTime.now().toString());
        map.put("protocol", methods[new Random().nextInt(100) % methods.length]);
        map.put("requestUser", requestUsers[new Random().nextInt(100) % requestUsers.length]);

        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
