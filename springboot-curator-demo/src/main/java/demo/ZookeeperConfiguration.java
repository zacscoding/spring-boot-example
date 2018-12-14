package demo;

import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zacconding
 * @Date 2018-12-14
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j(topic = "[ZOOKEEPER-CONFIG]")
@Configuration
public class ZookeeperConfiguration {

    private ZookeeperProperties zookeeperProperties;

    @Autowired
    public ZookeeperConfiguration(ZookeeperProperties zookeeperProperties) {
        this.zookeeperProperties = zookeeperProperties;
    }


    @PostConstruct
    public void setUp() {
        log.info(
            "\n// =======================================================\n"
                + "clientId : " + zookeeperProperties.getClientId() + "\n"
                + "address : " + zookeeperProperties.getAddress() + "\n"
                + "sleepMsBetweenRetries : " + zookeeperProperties.getSleepMsBetweenRetries() + "\n"
                + "maxRetries : " + zookeeperProperties.getMaxRetries() + "\n"
                + "======================================================= //\n"
        );
    }

    @Bean
    public CuratorFramework curatorFramework() {
        RetryPolicy retryPolicy = new RetryNTimes(
            zookeeperProperties.getMaxRetries(),
            zookeeperProperties.getSleepMsBetweenRetries());

        CuratorFramework client = CuratorFrameworkFactory.newClient(
            zookeeperProperties.getAddress(), retryPolicy);

        client.start();
        return client;
    }
}
