package demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-12-14
 * @GitHub : https://github.com/zacscoding
 */
@Component
public class ZookeeperProperties {

    @Value("${lock.client.id}")
    private String clientId;
    @Value("${cluster.zookeeper.address}")
    private String address;
    @Value("${cluster.zookeeper.maxRetries}")
    private int maxRetries;
    @Value("${cluster.zookeeper.sleepMsBetweenRetries}")
    private int sleepMsBetweenRetries;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public int getSleepMsBetweenRetries() {
        return sleepMsBetweenRetries;
    }

    public void setSleepMsBetweenRetries(int sleepMsBetweenRetries) {
        this.sleepMsBetweenRetries = sleepMsBetweenRetries;
    }
}
