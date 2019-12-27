package demo.starter.spring;

import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import demo.starter.core.RandomValueBuffer;
import demo.starter.core.RandomValueListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@ConditionalOnClass(RandomValueBuffer.class)
@EnableConfigurationProperties(StarterProperties.class)
@Slf4j
@RequiredArgsConstructor
public class StarterAutoConfiguration {

    private final StarterProperties properties;

    @PostConstruct
    private void setUp() {
        logger.info("Enable starter auto configuration. {}", properties);
    }

    // create a RandomValueListener bean if not exist
    @Bean
    @ConditionalOnMissingBean(RandomValueListener.class)
    public RandomValueListener randomValueListener() {
        logger.info("## Create a RandomValueListener bean");
        return values -> logger.info("onNewValues with size : {}", values.size());
    }

    // create a RandomValueBuffer bean if not exist
    @Bean
    @ConditionalOnMissingBean(RandomValueBuffer.class)
    public RandomValueBuffer buffer(RandomValueListener listener) {
        logger.info("## Create a RandomValueBuffer bean");

        final RandomValueBuffer buffer = new RandomValueBuffer(listener);

        buffer.start(properties.getMaxSize(), properties.getMaxTime());

        return buffer;
    }
}
