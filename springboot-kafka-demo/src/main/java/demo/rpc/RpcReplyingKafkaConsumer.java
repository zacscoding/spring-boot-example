package demo.rpc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-09-04
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Profile("rpc")
@Component
public class RpcReplyingKafkaConsumer {

    @KafkaListener(topics = "${rpc.kafka.topic.request-topic}")
    @SendTo
    public RpcResponse listen(RpcRequest request) {
        RpcResponse response = new RpcResponse(request.getNum1() + request.getNum2());
        // log.info("[Consumer] consume rpc request : {} - res : {}", request, response);
        return response;
    }
}
