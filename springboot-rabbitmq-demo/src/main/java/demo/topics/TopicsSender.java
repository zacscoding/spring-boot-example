package demo.topics;

import demo.model.DefaultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author zacconding
 * @Date 2018-08-07
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class TopicsSender {

    private final String[] keys = {"quick.orange.rabbit", "lazy.orange.elephant", "quick.orange.fox", "lazy.brown.fox", "lazy.pink.rabbit", "quick.brown.fox"};

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private TopicExchange topic;

    private int messageIdx = 0;

    @Scheduled(fixedDelay = 3000L, initialDelay = 1000L)
    public void sendMessage() {

        int keyIdx = messageIdx % keys.length;
        String key = keys[keyIdx];
        DefaultMessage sendMessage = new DefaultMessage(messageIdx, "Message-" + messageIdx + " >>>> key : " + key);

        StringBuilder sb = new StringBuilder("\n// ==================================================\n").append("[[ Topics Sender ]]").append("Thread : ")
                                                                                                         .append(Thread.currentThread().getName()).append("(")
                                                                                                         .append(Thread.currentThread().getId()).append(")\n")
                                                                                                         .append("Key : ").append(key).append("\n")
                                                                                                         .append("Send message : ")
                                                                                                         .append(sendMessage.toString()).append("\n").append(
                "===================================================== //\n");
        log.info(sb.toString());

        rabbitTemplate.convertAndSend(topic.getName(), key, sendMessage);
        messageIdx++;
    }
}
