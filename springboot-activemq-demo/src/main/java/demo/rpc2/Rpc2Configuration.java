package demo.rpc2;

import javax.jms.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

/**
 * @author zacconding
 * @Date 2018-09-17
 * @GitHub : https://github.com/zacscoding
 */
@Profile("rpc2")
@Slf4j
@EnableJms
@Configuration
public class Rpc2Configuration {

    @Bean
    public ActiveMQQueue requestDestination() {
        return new ActiveMQQueue("requestQueue");
    }

    @Bean
    public ActiveMQQueue replyDestination() {
        return new ActiveMQQueue("replyDestination");
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        System.out.println("## Check default connection factory :: " + connectionFactory.toString());
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setReceiveTimeout(10000L);
        return jmsTemplate;
    }

    @Bean
    public Rpc2Client rpc2Client(ConnectionFactory connectionFactory) {
        return new Rpc2Client(jmsTemplate(connectionFactory), requestDestination(), replyDestination());
    }

    @Bean
    public Rpc2Server rpc2Server(ConnectionFactory connectionFactory) {
        return new Rpc2Server(jmsTemplate(connectionFactory));
    }

    @Bean
    public DefaultMessageListenerContainer jmsContainerRequest(ConnectionFactory connectionFactory, Rpc2Server rpc2Server) {
        DefaultMessageListenerContainer listenerContainer = new DefaultMessageListenerContainer();

        listenerContainer.setConnectionFactory(connectionFactory);
        listenerContainer.setDestination(requestDestination());
        listenerContainer.setMessageListener(rpc2Server);

        return listenerContainer;
    }

    @Bean
    public DefaultMessageListenerContainer jmsContainerReply(ConnectionFactory connectionFactory, Rpc2Client rpc2Client) {
        DefaultMessageListenerContainer listenerContainer = new DefaultMessageListenerContainer();

        listenerContainer.setConnectionFactory(connectionFactory);
        listenerContainer.setDestination(replyDestination());
        listenerContainer.setMessageListener(rpc2Client);

        return listenerContainer;
    }
}
