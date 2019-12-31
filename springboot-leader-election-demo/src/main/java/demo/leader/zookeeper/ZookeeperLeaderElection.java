package demo.leader.zookeeper;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.springframework.beans.factory.InitializingBean;

import demo.leader.LeaderElection;
import demo.leader.LeaderElectionListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZookeeperLeaderElection implements LeaderElection, LeaderSelectorListener, InitializingBean {

    // required
    private final CuratorFramework curatorFramework;
    private final String lockPath;
    private Optional<LeaderElectionListener> listenerOptional;

    // builded
    private CountDownLatch leaderHolder;
    private LeaderSelector leaderSelector;

    public ZookeeperLeaderElection(CuratorFramework curatorFramework,
                                   String lockName,
                                   LeaderElectionListener listener) {

        this.curatorFramework = curatorFramework;
        lockPath = "/mutex/select/leader/" + lockName;
        listenerOptional = Optional.of(listener);
    }

    public void setLeaderElectionListener(LeaderElectionListener listener) {
        listenerOptional = Optional.of(listener);
    }

    @Override
    public void start() {
        if (leaderSelector != null) {
            logger.warn("ZookeeperLeaderElection already started");
            return;
        }

        leaderSelector = new LeaderSelector(curatorFramework, lockPath, this);
        leaderHolder = new CountDownLatch(1);
        leaderSelector.start();
    }

    @Override
    public void shutdown() {
        if (leaderSelector == null) {
            logger.warn("ZookeeperLeaderElection already stopped");
            return;
        }

        leaderHolder.countDown();
        //leaderSelector.close();
        leaderSelector = null;
    }

    @Override
    public boolean isLeader() {
        return leaderSelector.hasLeadership();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }

    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {
        listenerOptional.ifPresent(LeaderElectionListener::onTaskLeadership);
        leaderHolder.await();
        logger.info("ZookeeperLeaderElection release leader ship lock");
    }

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {
        logger.debug("state changed. new state : {}", newState);

        switch (newState) {
            case SUSPENDED:
            case LOST:
                logger.info("ConnectionState is SUSPEND OR LOST > {} : {}", curatorFramework, newState);
                leaderHolder.countDown();
        }
    }
}
