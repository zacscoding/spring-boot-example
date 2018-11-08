package demo;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-11-08
 * @GitHub : https://github.com/zacscoding
 */
@Component
public class DefaultComponent {

    private static final Logger logger = LoggerFactory.getLogger(DefaultComponent.class);

    @PostConstruct
    private void setUp() {
        logger.info("DefaultComponent is initialized");
    }
}
