package demo.stomp;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

/**
 * @author zacconding
 * @Date 2018-08-09
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Controller
@Profile("stomp")
public class GreetingController {

    @Autowired
    private SimpMessagingTemplate template;


    @MessageMapping("/static/hello")   // if a message is sent to destination "/hello", then the greeting() method is called.
    @SendTo("/topic/greetings") // The return value is broadcast to all subscribers to "/topic/greetings"
    public Greeting greeting(HelloMessage message) {
        try {
            log.info("## Receive static greeting : {}", message);
            TimeUnit.MILLISECONDS.sleep(500L);
            Greeting result = new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
            log.info("=====> Result : {}", result);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @MessageMapping("/dynamic/hello2/{name}")
    // @SendTo("/topic/greetings")
    public void dynamicGreeting(@DestinationVariable String name, HelloMessage message) {
        try {
            IntStream.range(1, 5).forEach(i -> {
                Greeting result = new Greeting("Dynamic Greeting, " + HtmlUtils.htmlEscape(message.getName()) + "- " + i + "!!");
                log.info("## Receive dynamic message. name in path : {}, hello message : {}, greeting : {}", name, message, result);
                template.convertAndSend("/topic/dynamic/" + name, result);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}