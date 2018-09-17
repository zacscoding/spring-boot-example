package demo.rpc;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

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
        return new Queue("rpc.requests");
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

    // client
    @Bean
    public RpcClient client() {
        // return new SingleRpcClient();
        return new MultipleRpcClient();
    }
}