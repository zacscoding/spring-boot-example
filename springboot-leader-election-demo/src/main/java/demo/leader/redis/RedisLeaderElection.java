package demo.leader.redis;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;

import demo.leader.LeaderElection;
import demo.leader.LeaderElectionListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedisLeaderElection implements LeaderElection, InitializingBean {

    private final RedissonClient redissonClient;
    private final String leadershipPath;
    private Optional<LeaderElectionListener> listenerOptional;
    private ElectionHolder holder;

    public RedisLeaderElection(RedissonClient redissonClient, String leadershipPath,
                               LeaderElectionListener listener) {

        this.redissonClient = requireNonNull(redissonClient, "redissonClient");
        this.leadershipPath = requireNonNull(leadershipPath, "leadershipPath");
        listenerOptional = Optional.ofNullable(listener);
    }

    public void setLeaderSelectionListener(LeaderElectionListener listener) {
        listenerOptional = Optional.ofNullable(listener);
    }

    @Override
    public void start() {
        if (holder != null) {
            logger.warn("RedisLeaderElection already started");
            return;
        }

        holder = new ElectionHolder(redissonClient, listenerOptional, leadershipPath);
        holder.start();
        logger.info("Success to start redis leader election");
    }

    @Override
    public void shutdown() {
        if (holder == null) {
            logger.warn("RedisLeaderElection already stopped");
            return;
        }

        holder.interrupt();
        holder = null;
        logger.info("Success to stop redis leader election");
    }

    @Override
    public boolean isLeader() {
        if (holder == null || holder.isInterrupted()) {
            holder = null;
            throw new IllegalStateException("Must started before getting leader ship");
        }

        return holder.hasLeadership();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }

    private static class ElectionHolder extends Thread {
        private static final long ACQUIRE_LOCK_TIME_SECONDS = 3L;

        private final Object masterLock = new Object();
        private RLock leaderLock;
        private boolean isLeader;

        // required
        private final Optional<LeaderElectionListener> listenerOptional;
        private final String lockName;

        public ElectionHolder(RedissonClient client,
                              Optional<LeaderElectionListener> listenerOptional,
                              String lockName) {

            this.listenerOptional = listenerOptional;
            this.lockName = lockName;

            leaderLock = client.getLock(lockName);
            setDaemon(true);
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    if (isLeader) {
                        synchronized (masterLock) {
                            masterLock.wait();
                            continue;
                        }
                    }

                    try {
                        isLeader = leaderLock.tryLock(ACQUIRE_LOCK_TIME_SECONDS, TimeUnit.SECONDS);
                        if (isLeader) {
                            logger.info("Take leadership {}", lockName);
                            listenerOptional.ifPresent(LeaderElectionListener::onTaskLeadership);
                        }
                    } catch (InterruptedException e) {
                        throw e;
                    } catch (Exception e) {
                        logger.warn("Exception occur while taking leadership", e);
                        TimeUnit.SECONDS.sleep(ACQUIRE_LOCK_TIME_SECONDS);
                    }
                }
            } catch (InterruptedException ignored) {
                // ignored
            } finally {
                if (leaderLock.isLocked() && leaderLock.isHeldByCurrentThread()) {
                    leaderLock.unlock();
                }
            }
        }

        public boolean hasLeadership() {
            return isLeader;
        }
    }
}
