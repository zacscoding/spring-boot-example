package demo.helloworld;

import demo.model.DefaultMessage;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-08-04
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Profile("hello-world")
@RabbitListener(queues = "hello")
public class HelloWorldReceiver {

    @RabbitHandler
    private void receiveMessage(DefaultMessage receiveMessage) {
        StringBuilder sb = new StringBuilder("\n// ==================================================\n").append("[[ Receiver ]]\n").append("Thread : ")
                                                                                                         .append(Thread.currentThread().getName()).append("(")
                                                                                                         .append(Thread.currentThread().getId()).append(")\n")
                                                                                                         .append("Receive message : ")
                                                                                                         .append(receiveMessage.toString()).append("\n").append(
                "===================================================== //\n");

        log.info(sb.toString());
    }
}
