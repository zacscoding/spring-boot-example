package demo.routing;

import demo.model.DefaultMessage;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author zacconding
 * @Date 2018-08-06
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@RabbitListener(queues = "#{autoDeleteQueue1.name}")
public class RoutingReceiver1 {

    @Value("#{autoDeleteQueue1.name}")
    private String queName;

    @PostConstruct
    private void setUp() {
        log.info("## RoutingReceiver1`s queue name : " + queName);
    }

    @RabbitHandler
    private void receiveMessage(DefaultMessage receiveMessage) {
        StringBuilder sb = new StringBuilder("\n// ==================================================\n").append("[[ Routing Receiver1 ]]\n")
                                                                                                         .append("Thread : ")
                                                                                                         .append(Thread.currentThread().getName()).append("(")
                                                                                                         .append(Thread.currentThread().getId()).append(")\n")
                                                                                                         .append("Receive message : ")
                                                                                                         .append(receiveMessage.toString()).append("\n").append(
                "===================================================== //\n");

        log.info(sb.toString());
    }
}
