package demo.uniquemessage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-12-19
 * @GitHub : https://github.com/zacscoding
 */
@Profile("uniquemessage")
@Slf4j(topic = "[CONSUMER]")
@Component
public class UniqueMessageConsumer {

    @KafkaListener(topics = "unique-message", groupId = "1")
    /*public void listen(@Payload UniqueMessage message
        , @Headers MessageHeaders headers) {*/
    // public void listen(@Payload UniqueMessage message) {
    public void listen(@Payload String message) {
        /*log.info("## [Consumer] receive message. id : {} / content : {}"
            , message.getId(), message.getContent());*/

        log.info("## [Consumer] receive message. {}"
            , message);
    }
}
