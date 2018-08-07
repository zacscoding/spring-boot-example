package demo.topics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author zacconding
 * @Date 2018-08-07
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Profile("topics")
@Configuration
public class TopicsConfiguration {

    // topic
    @Bean
    public TopicExchange topic() {
        return new TopicExchange("tut.topic");
    }

    // queues
    @Bean
    public Queue autoDeleteQueue1() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue autoDeleteQueue2() {
        return new AnonymousQueue();
    }


    // bindings
    @Bean
    public Binding binding1a() {
        return BindingBuilder.bind(autoDeleteQueue1()).to(topic()).with("*.orange.*");
    }

    @Bean
    public Binding binding2a() {
        return BindingBuilder.bind(autoDeleteQueue2()).to(topic()).with("*.*.rabbit");
    }

    @Bean
    public Binding binding2b() {
        return BindingBuilder.bind(autoDeleteQueue2()).to(topic()).with("lazy.#");
    }

    // receivers
    @Bean
    public TopicsReceiver1 receiver1() {
        return new TopicsReceiver1();
    }

    @Bean
    public TopicsReceiver2 receiver2() {
        return new TopicsReceiver2();
    }

    // sender
    @Bean
    public TopicsSender sender() {
        return new TopicsSender();
    }
}
