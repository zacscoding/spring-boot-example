package demo.stomp;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * @author zacconding
 * @Date 2018-08-12
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Controller
@Profile("stomp")
public class ActionController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/action/{moduleName}/{actionType}")
    public void doAction(@DestinationVariable("moduleName") String moduleName, @DestinationVariable("actionType") String actionType) {
        log.info("## Request action. module name : {} | action type : {}", moduleName, actionType);
        doActionInternal(moduleName, actionType);
    }

    private void doActionInternal(String moduleName, String actionType) {
        String destination = new StringBuilder("/result/action/").append(moduleName).append("/").append(actionType).toString();
        try {
            if ("start".equalsIgnoreCase(actionType)) {
                messagingTemplate.convertAndSend(destination, ">>> Try to start " + moduleName);
                TimeUnit.SECONDS.sleep(1L);
                messagingTemplate.convertAndSend(destination, ">>> Check validation. ");
                TimeUnit.SECONDS.sleep(1L);
                messagingTemplate.convertAndSend(destination, ">>>>> Success to start [ " + moduleName + " ]");
            } else if ("stop".equalsIgnoreCase(actionType)) {
                messagingTemplate.convertAndSend(destination, ">>> Try to stop " + moduleName);
                TimeUnit.SECONDS.sleep(1L);
                messagingTemplate.convertAndSend(destination, ">>> Check validation. ");
                TimeUnit.SECONDS.sleep(1L);

                boolean isRunning = new Random().nextInt(10) < 3;
                if (isRunning) {
                    messagingTemplate.convertAndSend(destination, ">>>>> Success to stop [" + moduleName + " ]");
                } else {
                    messagingTemplate.convertAndSend(destination, ">>>>> Already not working [ " + moduleName + " ]");
                }
            } else {
                messagingTemplate.convertAndSend(destination, ">>>>> Not supported action type : " + actionType);
            }
        } catch (InterruptedException e) {
            log.error("InterruptedException occur while execute action.");
        } finally {
            messagingTemplate.convertAndSend(destination, "COMPLETE");
        }

    }
}
