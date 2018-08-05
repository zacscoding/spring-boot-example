package demo.workqueues;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author zacconding
 * @Date 2018-08-05
 * @GitHub : https://github.com/zacscoding
 */
@Configuration
@Profile("work-queues")
@Slf4j
public class WorkQueuesConfiguration {
    static final String queueName = "workqueues";

    @Bean
    public Queue queue() {
        return new Queue(queueName);
    }

    @Bean
    public WorkQueuesReceiver receiver1() {
        return new WorkQueuesReceiver("1");
    }

    @Bean
    public WorkQueuesReceiver receiver2() {
        return new WorkQueuesReceiver("2");
    }

    @Bean
    public WorkQueuesSender sender() {
        return new WorkQueuesSender();
    }
}
