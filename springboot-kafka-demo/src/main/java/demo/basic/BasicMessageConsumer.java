package demo.basic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-09-01
 * @GitHub : https://github.com/zacscoding
 */
@Profile("basic")
@Slf4j
@Component
public class BasicMessageConsumer {

    @KafkaListener(topics = "zaccoding", groupId = "1")
    public void listen(String message) {
        log.info("## [Consumer] receive message : " + message);
    }
}
