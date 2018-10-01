package demo.rpc2;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-10-01
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Profile("rpc2")
@Component
public class SingleClient {

    @Autowired
    private Rpc2Client client;

    private AtomicLong requestId = new AtomicLong(1L);

    @Scheduled(fixedDelay = 3000L, initialDelay = 1000L)
    public void sendTask() {
        String uuid = UUID.randomUUID().toString();
        Rpc2Request req = new Rpc2Request();
        req.setRequestId(String.valueOf(requestId.getAndIncrement()));
        req.setRawRequest(uuid);

        Rpc2Response res = client.call(req);

        StringBuilder sb = new StringBuilder("\n// ==================================================\n")
            .append("[[ RPC CLIENT ]]")
            .append("Thread : ").append(Thread.currentThread().getName()).append("(").append(Thread.currentThread().getId()).append(")\n")
            .append("Request : ").append(req.toString()).append("\n")
            .append("Response : ").append(res.toString()).append("\n")
            .append("===================================================== //\n");
        log.info(sb.toString());
    }
}
