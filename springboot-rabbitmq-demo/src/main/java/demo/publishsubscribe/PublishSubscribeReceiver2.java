package demo.publishsubscribe;

import demo.model.DefaultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author zacconding
 * @Date 2018-08-06
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@RabbitListener(queues = "#{autoDeleteQueue2.name}")
public class PublishSubscribeReceiver2 {

    @RabbitHandler
    private void receiveMessage(DefaultMessage receiveMessage) {
        StringBuilder sb = new StringBuilder("\n// ==================================================\n").append("[[ Publish-Subscribe Receiver2 ]]\n")
                                                                                                         .append("Thread : ")
                                                                                                         .append(Thread.currentThread().getName()).append("(")
                                                                                                         .append(Thread.currentThread().getId()).append(")\n")
                                                                                                         .append("Receive message : ")
                                                                                                         .append(receiveMessage.toString()).append("\n").append(
                "===================================================== //\n");

        log.info(sb.toString());
    }
}
