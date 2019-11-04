package grpcdemo.hello;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 *
 * @GitHub : https://github.com/zacscoding
 */
@Data
@Component
@ConditionalOnProperty(name = "hello.enabled", havingValue = "true")
@ConfigurationProperties(prefix = "hello")
public class HelloServiceProperties {

    private int port;
}
