package demo.rpc;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zacconding
 * @Date 2018-09-21
 * @GitHub : https://github.com/zacscoding
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@ActiveProfiles("rpc2")
public class PropertiesTest {

    @Autowired
    private Environment environment;

    @Test
    public void readAndPrint() throws Exception {
        Properties properties = new Properties();
        properties.load(new ClassPathResource("application.properties").getInputStream());
        Set keySet = properties.keySet();
        for (Object o : keySet) {
            String key = (String)o;
            if (key.startsWith("spring.activemq") || key.startsWith("jms")) {
                System.out.println("\"" + key + "\",");
            }
        }
    }

    @Test
    public void profileTest() {
        List<String> keys = Arrays.asList(
            "spring.activemq.pool.expiry-timeout",
            "jms.template.default-destination",
            "spring.activemq.non-blocking-redelivery",
            "spring.activemq.pool.create-connection-on-startup",
            "spring.activemq.pool.block-if-full-timeout",
            "spring.activemq.broker-url",
            "spring.activemq.pool.reconnect-on-exception",
            "jms.template.receive-timeout",
            "jms.pub-sub-domain",
            "spring.activemq.packages.trusted",
            "jms.template.time-to-live",
            "spring.activemq.in-memory",
            "spring.activemq.pool.idle-timeout",
            "jms.listener.concurrency",
            "spring.activemq.pool.enabled",
            "jms.template.delivery-mode",
            "spring.activemq.pool.block-if-full",
            "jms.listener.max-concurrency",
            "spring.activemq.pool.time-between-expiration-check",
            "jms.listener.auto-startup",
            "spring.activemq.pool.maximum-active-session-per-connection",
            "spring.activemq.pool.use-anonymous-producers",
            "spring.activemq.packages.trust-all",
            "spring.activemq.pool.max-connections",
            "jms.template.priority",
            "jms.listener.acknowledge-mode",
            "jms.template.qos-enabled"
        );

        for (String property : keys) {
            log.info("## {} : {}", property, environment.getProperty(property));
        }
    }
}
