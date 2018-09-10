package demo.guide;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-09-10
 * @GitHub : https://github.com/zacscoding
 */
@Profile("guide")
@Component
@Slf4j
public class GuideReceiver {

    @JmsListener(destination = "mailbox", containerFactory = "myFactory")
    public void receiveMessage(GuideEmail email) {
        log.info("[Receiver] receive email : {}", email);
    }
}