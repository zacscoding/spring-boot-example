package demo.routing;

import demo.model.DefaultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author zacconding
 * @Date 2018-08-06
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class RoutingSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DirectExchange directExchange;

    private final String[] keys = {"orange", "black", "green"};
    private int index = 0;

    @Scheduled(fixedDelay = 3000L, initialDelay = 1000L)
    public void sendMessage() {
        int keyIdx = index % keys.length;
        String key = keys[keyIdx];
        DefaultMessage sendMessage = new DefaultMessage(index, "Message-" + index + ":" + key);


        StringBuilder sb = new StringBuilder("\n// ==================================================\n")
            .append("[[ Routing Sender ]]")
            .append("Thread : ").append(Thread.currentThread().getName()).append("(").append(Thread.currentThread().getId()).append(")\n")
            .append("Key : ").append(key).append("\n")
            .append("Send message : ").append(sendMessage.toString()).append("\n")
            .append("===================================================== //\n");
        log.info(sb.toString());

        rabbitTemplate.convertAndSend(directExchange.getName(), key, sendMessage);
        index++;
    }
}