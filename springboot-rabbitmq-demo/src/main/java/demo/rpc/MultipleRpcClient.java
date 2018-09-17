package demo.rpc;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-09-17
 * @GitHub : https://github.com/zacscoding
 */
@Profile("rpc")
@Slf4j
public class MultipleRpcClient implements RpcClient {

    private AtomicInteger req = new AtomicInteger(0);
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DirectExchange exchange;

    @Scheduled(fixedDelay = 3000L, initialDelay = 1000L)
    public void send() throws Exception {
        log.info(">> MultipleRpcClient execute..");

        final int threadCount = new Random().nextInt(500) + 1;
        RequestTask[] tasks = new RequestTask[threadCount];
        for (int i = 0; i < threadCount; i++) {
            tasks[i] = new RequestTask(rabbitTemplate, exchange, req.incrementAndGet());
            tasks[i].setDaemon(true);
            tasks[i].start();
        }

        for (int i = 0; i < threadCount; i++) {
            tasks[i].join();
        }

        int fail = 0;
        for (int i = 0; i < threadCount; i++) {
            if (!tasks[i].isSuccess()) {
                fail++;
            }
        }
        log.info("Request : {} | success : {} | fail : {}", threadCount, (threadCount - fail), fail);
    }

    public static class RequestTask extends Thread {

        private RabbitTemplate rabbitTemplate;
        private DirectExchange exchange;
        private int requestId;
        private boolean success = false;

        public RequestTask(RabbitTemplate rabbitTemplate, DirectExchange exchange, int requestId) {
            this.rabbitTemplate = rabbitTemplate;
            this.exchange = exchange;
            this.requestId = requestId;
        }

        public void run() {
            Integer response = (Integer) rabbitTemplate.convertSendAndReceive(exchange.getName(), "rpc", requestId);
            if (response == null || requestId != response.intValue()) {
                log.warn("## Find different value.. req : " + requestId + " , res : " + response);
            } else {
                success = true;
            }
        }

        public boolean isSuccess() {
            return success;
        }
    }


}
