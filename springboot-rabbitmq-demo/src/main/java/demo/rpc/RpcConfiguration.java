package demo.rpc;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author zacconding
 * @Date 2018-08-08
 * @GitHub : https://github.com/zacscoding
 */
@Configuration
@Profile("rpc")
public class RpcConfiguration {

    // exchange
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("tut.rpc");
    }

    @Bean
    public Queue queue() {
        return new Queue("rp1c.requests");
    }

    // server
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with("rpc");
    }

    @Bean
    public RpcServer server() {
        return new RpcServer();
    }


    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("rpc.requests");

        // container.setMessageListener(server2());
        container.setMessageListener(listenerAdapter);

        // container.setConcurrency("100");
        container.setConcurrency("5");
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter() {
        return new MessageListenerAdapter(server(), "multiply");
        // return new MessageListenerAdapter(server2(), "multiply");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        System.out.println("## Check connection factory.. " + connectionFactory.getClass().getName());
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setReceiveTimeout(5000L);
        return rabbitTemplate;
    }

    // client
    @Bean
    public RpcClient client() {
        return new SingleRpcClient();
        // return new MultipleRpcClient();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(500);
        executor.setThreadNamePrefix("rpc-server");
        executor.initialize();
        executor.setDaemon(true);
        return executor;
    }
}