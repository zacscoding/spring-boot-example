package demo.workqueues;

import demo.model.DefaultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author zacconding
 * @Date 2018-08-05
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
public class WorkQueuesSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private Queue queue;
    private long index = 0L;

    @Scheduled(fixedDelay = 4000L, initialDelay = 500)
    public void sendTask() {
        DefaultMessage sendMessage = new DefaultMessage(index, "Message-" + index);

        StringBuilder sb = new StringBuilder("\n// ==================================================\n")
            .append("[[ WorkQueues Sender ]]")
            .append("Thread : ").append(Thread.currentThread().getName()).append("(").append(Thread.currentThread().getId()).append(")\n")
            .append("Send message : ").append(sendMessage.toString()).append("\n")
            .append("===================================================== //\n");
        log.info(sb.toString());

        rabbitTemplate.convertAndSend(queue.getName(), sendMessage);
        index += 1L;
    }
}
