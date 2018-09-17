package demo.rpc;

import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.Environment;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-08-08
 * @GitHub : https://github.com/zacscoding
 */
@Profile("rpc")
@Slf4j
public class SingleRpcClient implements RpcClient {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DirectExchange exchange;

    int start = 0;

    @Scheduled(fixedDelay = 3000L, initialDelay = 1000L)
    public void send() {
        log.info(">> RpcClient execute..");
        int request = start++;
        Integer response = (Integer) rabbitTemplate.convertSendAndReceive(exchange.getName(), "rpc", request);

        StringBuilder sb = new StringBuilder("\n// ==================================================\n")
            .append("[[ RPC CLIENT ]]")
            .append("Thread : ").append(Thread.currentThread().getName()).append("(").append(Thread.currentThread().getId()).append(")\n")
            .append("Request : ").append(request).append("\n")
            .append("Response : ").append(response).append("\n")
            .append("===================================================== //\n");

        log.info(sb.toString());
    }
}