package demo.rpc2;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author zacconding
 * @Date 2018-10-01
 * @GitHub : https://github.com/zacscoding
 */
public class DefaultRpc2Client implements Rpc2Client {

    private RabbitTemplate rabbitTemplate;
    private DirectExchange exchange;
    private String routeKey;

    public DefaultRpc2Client(RabbitTemplate rabbitTemplate, DirectExchange exchange, String routeKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routeKey = routeKey;
    }

    @Override
    public Rpc2Response call(Rpc2Request request) {
        Rpc2Response res = (Rpc2Response) rabbitTemplate.convertSendAndReceive(exchange.getName(), routeKey, request);
        return res;
    }
}
