package demo.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-09-01
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Component
public class MessageConsumer {

    @KafkaListener(topics = "zaccoding", groupId = "1")
    public void listen(String message) {
        log.info("[Consumer] receive message : " + message);
    }
}
