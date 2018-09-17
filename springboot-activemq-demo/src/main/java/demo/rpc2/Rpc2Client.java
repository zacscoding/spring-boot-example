package demo.rpc2;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * @author zacconding
 * @Date 2018-09-17
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j(topic = "producer")
public class Rpc2Client implements MessageListener {

    private static ConcurrentMap<String, ReplyMessage> concurrentMap = new ConcurrentHashMap<String, ReplyMessage>();

    private JmsTemplate jmsTemplate;
    private Destination requestDestination;
    private Destination replyDestination;

    @Autowired
    public Rpc2Client(JmsTemplate jmsTemplate, Destination requestDestination, Destination replyDestination) {
        this.jmsTemplate = jmsTemplate;
        this.requestDestination = requestDestination;
        this.replyDestination = replyDestination;
    }

    public String processRequest(String message) {
        ReplyMessage replyMessage = new ReplyMessage();
        final String correlationID = UUID.randomUUID().toString();
        concurrentMap.put(correlationID, replyMessage);

        jmsTemplate.send(requestDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                Message requestMessage = session.createTextMessage(message);

                requestMessage.setJMSCorrelationID(correlationID);
                requestMessage.setJMSReplyTo(replyDestination);

                return requestMessage;
            }
        });

        try {
            // boolean isReceived = replyMessage.getSemaphore().tryAcquire(10, TimeUnit.SECONDS);
            boolean isReceived = replyMessage.getCountDownLatch().await(3, TimeUnit.SECONDS);
            ReplyMessage result = concurrentMap.remove(correlationID);

            if (isReceived && result != null) {
                Message responseMessage = result.getMessage();
                return (responseMessage == null) ? null : ((TextMessage) responseMessage).getText();
            }
        } catch (InterruptedException e) {
            log.warn("InterruptedException occur");
        } catch (JMSException e) {
            log.warn("JMSException occur while getting reply message", e);
        }

        return null;
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                ReplyMessage result = concurrentMap.get(textMessage.getJMSCorrelationID());
                if (result == null) {
                    log.warn("Already removed after timeout : message :: " + textMessage.getText());
                } else {
                    result.setMessage(textMessage);
                    // result.getSemaphore().release();
                    result.getCountDownLatch().countDown();
                }
                log.info("onMessage : {}", textMessage.getText());
            } catch (JMSException e) {
                log.warn("JMSException occur while handle on message", e);
            }
        }
    }
}
