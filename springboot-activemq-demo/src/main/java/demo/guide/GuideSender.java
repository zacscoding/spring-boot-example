package demo.guide;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-09-10
 * @GitHub : https://github.com/zacscoding
 */
@Profile("guide")
@Slf4j
@Component
public class GuideSender {

    private JmsTemplate jmsTemplate;

    public GuideSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @PostConstruct
    private void setUp() {
        Thread t = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String to = UUID.randomUUID().toString();
                    String body = "body :: " + UUID.randomUUID().toString();

                    GuideEmail email = new GuideEmail(to, body);
                    log.info("[Producer] email : {}", email);
                    jmsTemplate.convertAndSend("mailbox", email);
                    TimeUnit.SECONDS.sleep(2L);
                }
            } catch (InterruptedException e) {
                return;
            }

        });

        t.setDaemon(true);
        t.start();
    }
}
