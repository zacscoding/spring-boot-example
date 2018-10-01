package demo.rpc2;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author zacconding
 * @Date 2018-10-01
 * @GitHub : https://github.com/zacscoding
 */
@Profile("rpc2")
@Configuration
public class Rpc2Configuration {

    // exchange
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("tut.rpc2");
    }

    @Bean
    public Queue queue() {
        return new Queue("rpc.requests2");
    }

    // server
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with("rpc2");
    }

    @Bean
    public Jackson2JsonMessageConverter producerMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Rpc2Server rpcServer(RabbitTemplate rabbitTemplate) {
        return new Rpc2Server(rabbitTemplate);
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory, Rpc2Server rpc2Server) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("rpc.requests2");
        container.setConcurrency("5");
        container.setMessageListener(rpc2Server);

        return container;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();

        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setReceiveTimeout(5000L);
        rabbitTemplate.setMessageConverter(producerMessageConverter());

        return rabbitTemplate;
    }

    @Bean
    public Rpc2Client rpc2Client(RabbitTemplate rabbitTemplate) {
        return new DefaultRpc2Client(rabbitTemplate, exchange(), "rpc2");
    }
}
