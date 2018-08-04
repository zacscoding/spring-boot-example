package demo.configuration;

import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostConstruct
    private void setUp() {
        System.out.println("// ===============================================================");
        System.out.println("## Active profile : " + Arrays.toString(env.getActiveProfiles()));
        System.out.println("================================================================== //");
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();

        factory.setHost("192.168.5.77");
        factory.setUsername("test");
        factory.setPassword("test");

        return factory;
    }
}