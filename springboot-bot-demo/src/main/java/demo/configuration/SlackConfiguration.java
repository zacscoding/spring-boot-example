package demo.configuration;

import demo.configuration.properties.SlackProperties;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zacconding
 * @Date 2019-01-16
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "slack.enabled", havingValue = "true")
public class SlackConfiguration {

    @PostConstruct
    private void setUp() {
        log.info("## Enabled slack bot.");
    }
}
