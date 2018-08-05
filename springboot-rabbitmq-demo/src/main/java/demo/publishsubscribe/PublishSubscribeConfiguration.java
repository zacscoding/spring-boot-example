package demo.publishsubscribe;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author zacconding
 * @Date 2018-08-06
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Configuration
@Profile("publish-subscribe")
public class PublishSubscribeConfiguration {

    @Bean
    public FanoutExchange fanout() {
        return new FanoutExchange("tut.fanout");
    }

    @Bean
    public Queue autoDeleteQueue1() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue autoDeleteQueue2() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding binding1() {
        return BindingBuilder.bind(autoDeleteQueue1()).to(fanout());
    }

    @Bean
    public Binding binding2() {
        return BindingBuilder.bind(autoDeleteQueue2()).to(fanout());
    }

    @Bean
    public PublishSubscribeSender sender() {
        return new PublishSubscribeSender();
    }

    @Bean
    public PublishSubscribeReceiver1 receiver1() {
        return new PublishSubscribeReceiver1();
    }

    @Bean
    public PublishSubscribeReceiver2 receiver2() {
        return new PublishSubscribeReceiver2();
    }
}
