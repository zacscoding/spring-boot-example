package demo.sample;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.jms.Queue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-09-09
 * @GitHub : https://github.com/zacscoding
 */
@Profile("sample")
@Slf4j
@Component
public class SampleProducer {

    private JmsMessagingTemplate jmsMessagingTemplate;
    private Queue queue;

    @Autowired
    public SampleProducer(JmsMessagingTemplate jmsMessagingTemplate, Queue queue) {
        this.jmsMessagingTemplate = jmsMessagingTemplate;
        this.queue = queue;
    }

    @PostConstruct
    private void setUp() {
        Thread t = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String uuid = UUID.randomUUID().toString();
                    log.info("[Producer] send message : " + uuid);
                    send(uuid);
                    TimeUnit.SECONDS.sleep(3L);
                }
            } catch (InterruptedException e) {
            }
        });

        t.setDaemon(true);
        t.start();
    }

    private void send(String message) {
        this.jmsMessagingTemplate.convertAndSend(this.queue, message);
    }
}