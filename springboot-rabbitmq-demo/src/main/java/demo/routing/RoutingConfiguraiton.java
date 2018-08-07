package demo.routing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
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
@Profile("routing")
public class RoutingConfiguraiton {

    // direct
    @Bean
    public DirectExchange direct() {
        return new DirectExchange("tut.direct");
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
        return BindingBuilder.bind(autoDeleteQueue1()).to(direct()).with("orange");
    }

    @Bean
    public Binding binding1b() {
        return BindingBuilder.bind(autoDeleteQueue1()).to(direct()).with("black");
    }

    @Bean
    public Binding binding2a() {
        return BindingBuilder.bind(autoDeleteQueue2()).to(direct()).with("green");
    }

    @Bean
    public Binding binding2b() {
        return BindingBuilder.bind(autoDeleteQueue2()).to(direct()).with("black");
    }

    // receivers
    @Bean
    public RoutingReceiver1 receiver1() {
        return new RoutingReceiver1();
    }

    @Bean
    public RoutingReceiver2 receiver2() {
        return new RoutingReceiver2();
    }

    // sender
    @Bean
    public RoutingSender sender() {
        return new RoutingSender();
    }
}
