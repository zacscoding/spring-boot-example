package demo.rpc;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.jms.JMSException;
import javax.jms.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-09-10
 * @GitHub : https://github.com/zacscoding
 */
@Profile("rpc")
@Slf4j
@Component
public class RpcClient {

    private JmsTemplate jmsTemplate;
    private MessageConverter messageConverter;

    @Autowired
    private RpcClient(JmsTemplate jmsTemplate, MessageConverter messageConverter) {
        this.jmsTemplate = jmsTemplate;
        this.messageConverter = messageConverter;
    }

    public JsonRpcResponse processRequest(JsonRpcRequest request) {
        try {
            Message replyMessage = jmsTemplate.sendAndReceive("rpc.queue", session -> messageConverter.toMessage(request, session));
            // return null if receive null or timeout
            // return (replyMessage == null) ? null : (JsonRpcResponse) messageConverter.fromMessage(replyMessage);
            // TEMP FOR TEST
            JsonRpcResponse response = (replyMessage == null) ? null : (JsonRpcResponse) messageConverter.fromMessage(replyMessage);
            // log.info("\nrequest : {} ==> response : {}", request, response);
            // -- TEMP FOR TEST
            return response;
        } catch (JMSException e) {
            return new JsonRpcResponse(request.getId(), "error"); // TEMP
        }
    }
}