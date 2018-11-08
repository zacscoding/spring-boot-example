package demo;

import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-11-08
 * @GitHub : https://github.com/zacscoding
 */
@Component
public class RunnerComponent implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(RunnerComponent.class);

    @PostConstruct
    private void setUp() {
        logger.info("RunnerComponent is initialized");
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Try to run at RunnerComponent... {}", Arrays.toString(args));
    }
}