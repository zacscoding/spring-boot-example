package demo.stomp;

import ch.qos.logback.classic.Logger;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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

    @MessageMapping("/hello")   // if a message is sent to destination "/hello", then the greeting() method is called.
    @SendTo("/topic/greetings") // The return value is broadcast to all subscribers to "/topic/greetings"
    public Greeting greeting(HelloMessage message) {
        try {
            TimeUnit.SECONDS.sleep(1);
            Greeting result = new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");

            log.info("## Receive : {} ===> Result : {}", message, result);

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @MessageMapping("/dynamic/{name}")
    @SendTo("/topic/greetings")
    public Greeting dynamicGreeting(@DestinationVariable String name, HelloMessage message) {
        try {
            TimeUnit.SECONDS.sleep(1);

            Greeting result = new Greeting("Dynamic Greeting, " + HtmlUtils.htmlEscape(message.getName()) + "!");
            log.info("## Receive name in path : {}, heello message : {}, greeting : {}", name, message, result);

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}