package demo.basic;

import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-09-01
 * @GitHub : https://github.com/zacscoding
 */
@Profile("basic")
@Slf4j
@Component
public class BasicMesageProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private final String topic = "zaccoding";

    @PostConstruct
    private void setUp() {
        Thread sender = new Thread(() -> {
            try {
                int messageNumber = 0;
                while (!Thread.currentThread().isInterrupted()) {
                    String message = "Message-" + (++messageNumber);
                    log.info("[Try:: Message Producer] : " + message);
                    kafkaTemplate.send(topic, message);
                    TimeUnit.SECONDS.sleep(5);
                }
            } catch (InterruptedException e) {
                log.warn("Exception occur while producing messages", e);
            }
        });
        sender.setDaemon(true);
        sender.start();
    }
}
