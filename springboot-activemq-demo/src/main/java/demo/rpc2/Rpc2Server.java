package demo.rpc2;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * @author zacconding
 * @Date 2018-09-17
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j(topic = "consumer")
public class Rpc2Server implements MessageListener {

    private JmsTemplate jmsTemplate;
    private ExecutorService executorService;

    public Rpc2Server(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
        this.executorService = Executors.newFixedThreadPool(500);
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                final String request = textMessage.getText();
                log.info(request);
                Destination destination = textMessage.getJMSReplyTo();
                final String jmsCorrelationID = textMessage.getJMSCorrelationID();
                jmsTemplate.send(destination, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        String response = (request == null) ? "NULL" : request.toUpperCase();
                        Message msg = session.createTextMessage(response);
                        msg.setJMSCorrelationID(jmsCorrelationID);
                        return msg;
                    }
                });
            } catch (JMSException e) {
                log.warn("JMSException occur while handle on message", e);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
