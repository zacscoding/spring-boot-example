package demo.starter.spring;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 */
@ConfigurationProperties(prefix = "starter")
@Getter
@Setter
@ToString
public class StarterProperties {

    private int maxSize;
    private Duration maxTime;
}
