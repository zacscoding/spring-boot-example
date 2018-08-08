package demo.rpc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author zacconding
 * @Date 2018-08-08
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class RpcClient {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DirectExchange exchange;

    int start = 0;

    @Scheduled(fixedDelay = 3000L, initialDelay = 1000L)
    public void send() {
        int request = start++;
        Integer response = (Integer) rabbitTemplate.convertSendAndReceive(exchange.getName(), "rpc", request);

        StringBuilder sb = new StringBuilder("\n// ==================================================\n")
            .append("[[ RPC CLIENT ]]")
            .append("Thread : ").append(Thread.currentThread().getName()).append("(").append(Thread.currentThread().getId()).append(")\n")
            .append("Requset : ").append(request).append("\n")
            .append("Response : ").append(response).append("\n")
            .append("===================================================== //\n");

        log.info(sb.toString());
    }
}
