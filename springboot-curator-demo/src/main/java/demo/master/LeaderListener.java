package demo.master;

import demo.ZookeeperProperties;
import java.util.concurrent.CountDownLatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-12-15
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j(topic = "[LEADER]")
@Component
public class LeaderListener implements LeaderSelectorListener {

    private String fixedTaskMutexPath;
    private LeaderSelector leaderSelector;
    private CountDownLatch countDownLatch;

    // autowired
    private CuratorFramework curatorFramework;
    private ZookeeperProperties zookeeperProperties;

    @Autowired
    public LeaderListener(CuratorFramework curatorFramework,
        ZookeeperProperties zookeeperProperties) {

        this.curatorFramework = curatorFramework;
        this.zookeeperProperties = zookeeperProperties;
        this.countDownLatch = new CountDownLatch(1);
        this.fixedTaskMutexPath = "/mutex/select/leader/job/A";
        this.leaderSelector = new LeaderSelector(curatorFramework, fixedTaskMutexPath, this);
    }

    public void start() {
        leaderSelector.start();
    }

    public void close() {
        leaderSelector.close();
    }

    public boolean isLeader() {
        return leaderSelector.hasLeadership();
    }

    @Override
    public void takeLeadership(CuratorFramework internalCuratorFramework) throws Exception {
        log.info("this app`s client`s client : {}", curatorFramework);
        log.info("[{}] task leadership.... client : {}"
            , zookeeperProperties.getClientId(), internalCuratorFramework
        );
        countDownLatch.await();
        log.warn("{} is not a leader anymore", zookeeperProperties.getClientId());
    }

    @Override
    public void stateChanged(CuratorFramework curatorFramework, ConnectionState connectionState) {
        if ((connectionState == ConnectionState.SUSPENDED)
            || (connectionState == ConnectionState.LOST)) {

            log.info("ConnectionState is SUSPEND OR LOST > {} : {}"
                , curatorFramework, connectionState);

            countDownLatch.countDown();
        } else if ((connectionState == ConnectionState.CONNECTED)
            || (connectionState == ConnectionState.RECONNECTED)) {

            log.info("client {}. attempting for leadership : {}"
                , connectionState, zookeeperProperties.getClientId());
            countDownLatch = new CountDownLatch(1);
        }

        log.info("this app`s client : {}", curatorFramework);
        log.info("[{}] state changed...... >> {}",
            zookeeperProperties.getClientId(), connectionState
        );
    }
}