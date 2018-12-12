package demo.lock;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.apache.curator.framework.recipes.shared.VersionedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zacconding
 * @Date 2018-12-12
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j(topic = "[LOCK]")
@Profile("lock")
@RestController
public class LockTaskController {

    private final String sharedCountPath = "/lock/counters/task";
    private final String sharedLockPath = "/lock/mutex/task";
    private final Random random = new Random();

    private String clientId;
    private CuratorFramework curatorFramework;
    private InterProcessSemaphoreMutex sharedLock;
    private SharedCount sharedCount;

    @Autowired
    public LockTaskController(CuratorFramework curatorFramework, LockConfiguration lockConfiguration) {
        this.curatorFramework = curatorFramework;
        this.clientId = lockConfiguration.getClientId();
    }

    @PostConstruct
    private void setUp() throws Exception {
        this.sharedLock = new InterProcessSemaphoreMutex(curatorFramework, sharedLockPath);
        this.sharedCount = new SharedCount(curatorFramework, sharedCountPath, 1);
        this.sharedCount.start();
    }

    @GetMapping(value = "/lock/task/{taskNumber}")
    public ResponseEntity<Boolean> receiveTask(@PathVariable("taskNumber") long taskNumber) {
        Thread task = new Thread(() -> {
            try {
                sharedLock.acquire();
                doTask(taskNumber);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    sharedLock.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        task.setDaemon(true);
        task.start();
        return ResponseEntity.ok(Boolean.TRUE);
    }

    // @GetMapping(value = "/lock/task-with-number/{taskNumber}")

    private void doTask(long taskNumber) throws Exception {
        log.info("## Receive task : {} at {}", taskNumber, clientId);
        log.info("## > Acquire lock. client : {}, taskNumber : {}", clientId, taskNumber);
        if (sharedCount.getCount() >= taskNumber) {
            log.info("## >> Skip task. sharedCount : {} / taskNumber : {}", sharedCount.getCount(), taskNumber);
            return;
        }

        int sleep = random.nextInt(3) + 1;
        log.info("## >> do something. task number : {} / sleep : {} / client : {}", taskNumber, sleep, clientId);
        TimeUnit.SECONDS.sleep(sleep);

        sharedCount.setCount((int) taskNumber);
        log.info("## >>> After set count : {} / task number : {}", sharedCount.getCount(), taskNumber);
        log.info("## >>> Complete task. client : {} / task number : {} / sharedCount : {}", clientId,
                taskNumber, sharedCount.getCount());
    }
}
