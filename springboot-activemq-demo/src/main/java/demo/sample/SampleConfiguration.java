package demo.sample;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;

/**
 * @author zacconding
 * @Date 2018-09-09
 * @GitHub : https://github.com/zacscoding
 */
@Configuration
@EnableJms
@Profile("sample")
public class SampleConfiguration {

    @Bean
    public Queue queue() {
        return new ActiveMQQueue("sample.queue");
    }
}
