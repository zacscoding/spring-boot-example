package demo.helloworld;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author zacconding
 * @Date 2018-08-04
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Configuration
@Profile("hello-world")
public class HelloWorldConfiguration {

    static final String queueName = "hello";

    @Bean
    public Queue queue() {
        return new Queue(queueName, false);
    }

//    @Bean
//    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(queueName);
//        container.setMessageListener(listenerAdapter);
//
//        return container;
//    }
//
//    @Bean
//    public MessageListenerAdapter listenerAdapter(HelloWorldReceiver receiver) {
//        return new MessageListenerAdapter(receiver, "receiveMessage");
//    }

}
