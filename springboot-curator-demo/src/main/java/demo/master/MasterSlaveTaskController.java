package demo.master;

import demo.ZookeeperProperties;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zacconding
 * @Date 2018-12-14
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j(topic = "[MASTER-SLAVE]")
@Profile("master-slave")
@RestController
public class MasterSlaveTaskController {

    private ZookeeperProperties zookeeperProperties;
    private CuratorFramework curatorFramework;
    private String mutexPath = "/mutex/select/leader/job/";
    private LeaderListener leaderListener;

    @Autowired
    public MasterSlaveTaskController(CuratorFramework curatorFramework,
        ZookeeperProperties zookeeperProperties,
        LeaderListener leaderListener) {

        this.curatorFramework = curatorFramework;
        this.zookeeperProperties = zookeeperProperties;
        this.leaderListener = leaderListener;
    }

    @PostConstruct
    private void setUp() {
        leaderListener.start();

        // check leadership
        Thread leaderChecker = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    log.info("[Check leader - {}] is leader : {}"
                        , zookeeperProperties.getClientId()
                        , leaderListener.isLeader()
                    );
                    TimeUnit.SECONDS.sleep(1L);
                }
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        });
        leaderChecker.start();
    }


    @GetMapping("/master-slave/push/task/{taskNumber}")
    public ResponseEntity<Boolean> pushTask(@PathVariable("taskNumber") long taskNumber) {
        log.info("## [Push task. client id : {} | task number : {}\n{}",
            zookeeperProperties.getClientId(), taskNumber, curatorFramework);

        new LeaderSelector(curatorFramework, mutexPath + taskNumber,
            new LeaderSelectorListener() {
                @Override
                public void takeLeadership(CuratorFramework internalCuratorFramework)
                    throws Exception {

                    log.info("## [{}] - taskLeadership at client : {} - {}"
                        , zookeeperProperties.getClientId(), taskNumber, internalCuratorFramework);

                    int sleep = new Random().nextInt(3) + 1;
                    TimeUnit.SECONDS.sleep(sleep);

                    log.info("## [{}] - complete task : {}"
                        , zookeeperProperties.getClientId(), taskNumber);
                }

                @Override
                public void stateChanged(CuratorFramework curatorFramework,
                    ConnectionState connectionState) {

                    log.info("## [{}] stateChanged at client >> {}"
                        , zookeeperProperties.getClientId(), connectionState);
                }
            }).start();

        return ResponseEntity.ok(Boolean.TRUE);
    }
}
