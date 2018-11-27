package demo.publishsubscribe;

import demo.model.DefaultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author zacconding
 * @Date 2018-08-06
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class PublishSubscribeSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FanoutExchange fanoutExchange;

    private int index = 0;

    @Scheduled(fixedDelay = 4000L, initialDelay = 500)
    public void sendTask() {
        DefaultMessage sendMessage = new DefaultMessage(index, "Message-" + index);

        StringBuilder sb = new StringBuilder("\n// ==================================================\n").append("[[ Publish-Subscribe Sender ]]")
                                                                                                         .append("Thread : ")
                                                                                                         .append(Thread.currentThread().getName()).append("(")
                                                                                                         .append(Thread.currentThread().getId()).append(")\n")
                                                                                                         .append("Send message : ")
                                                                                                         .append(sendMessage.toString()).append("\n").append(
                "===================================================== //\n");
        log.info(sb.toString());

        rabbitTemplate.convertAndSend(fanoutExchange.getName(), "", sendMessage);
        index++;
    }
}
