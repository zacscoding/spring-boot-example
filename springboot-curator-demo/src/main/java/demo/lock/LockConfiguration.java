package demo.lock;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author zacconding
 * @Date 2018-12-12
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j(topic = "[LOCK-CONFIG]")
@Profile("lock")
@Configuration
public class LockConfiguration {

    @Value("${lock.client.id}")
    private String clientId;
    @Value("${cluster.zookeeper.address}")
    private String address;
    @Value("${cluster.zookeeper.maxRetries}")
    private int maxRetries;
    @Value("${cluster.zookeeper.sleepMsBetweenRetries}")
    private int sleepMsBetweenRetries;


    @PostConstruct
    public void setUp() {
        log.info("\n// =======================================================\n"
                + "clientId : " + clientId + "\n"
                + "address : " + address + "\n"
                + "sleepMsBetweenRetries : " + sleepMsBetweenRetries + "\n"
                + "maxRetries : " + maxRetries + "\n"
                + "======================================================= //\n"
        );
    }

    @Bean
    public CuratorFramework curatorFramework() {
        RetryPolicy retryPolicy = new RetryNTimes(maxRetries, sleepMsBetweenRetries);
        CuratorFramework client = CuratorFrameworkFactory.newClient(address, retryPolicy);
        client.start();
        return client;
    }

    public String getClientId() {
        return this.clientId;
    }
}
