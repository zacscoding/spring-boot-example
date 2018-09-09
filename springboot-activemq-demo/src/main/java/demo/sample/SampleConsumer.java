package demo.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-09-09
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Profile("sample")
@Component
public class SampleConsumer {

    @JmsListener(destination = "sample.queue")
    public void receiveMessage(String message) {
        log.info("[Consumer] receive message : " + message);
    }
}