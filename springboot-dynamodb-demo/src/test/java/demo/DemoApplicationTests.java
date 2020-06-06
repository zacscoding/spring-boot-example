package demo;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import demo.util.LogLevelUtil;

@SpringBootTest
@Testcontainers
class DemoApplicationTests {
    static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    static final String DOCKER_IMAGE = "amazon/dynamodb-local";
    static final String DOCKER_VERSION = "latest";
    static final int PORT = 8000;

    static {
        LogLevelUtil.setWarn("org.testcontainers");
        LogLevelUtil.setWarn("com.github.dockerjava");
    }

    @Container
    final GenericContainer dynamodb = new GenericContainer(String.format("%s:%s", DOCKER_IMAGE, DOCKER_VERSION))
            .withExposedPorts(PORT);

    @Test
    void contextLoads() throws Exception {
        logger.info("Info");
        logger.debug("debug");
        logger.warn("warn");
        logger.error("error");
        System.out.println(":)");
        TimeUnit.SECONDS.sleep(10L);
    }
}
