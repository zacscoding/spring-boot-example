package demo.configuration;

import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author zacconding
 * @Date 2018-08-04
 * @GitHub : https://github.com/zacscoding
 */
@Configuration
@EnableScheduling
public class RabbitMQConfiguration {

    @Autowired
    Environment env;

    @Value("${rabbitmq.host}")
    private String host;
    @Value("${rabbitmq.user}")
    private String user;
    @Value("${rabbitmq.password}")
    private String password;

    @PostConstruct
    private void setUp() {
        System.out.println("// ===============================================================");
        System.out.println("## Active profile : " + Arrays.toString(env.getActiveProfiles()));
        System.out.println("================================================================== //");
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();

        factory.setHost(host);
        factory.setUsername(user);
        factory.setPassword(password);

        return factory;
    }
}