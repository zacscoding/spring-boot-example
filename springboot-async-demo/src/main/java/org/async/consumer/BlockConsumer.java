package org.async.consumer;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;
import org.async.model.Block;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author zacconding
 * @Date 2018-04-22
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Component
public class BlockConsumer {

    private Thread thread;
    private boolean alive = true;
    private BlockingQueue<Block> blockingQueue;
    private List<DeferredResult<Block>> subscribers;
    private Block lastBlock;
    private ReentrantLock lock = new ReentrantLock();

    @Autowired
    public BlockConsumer(BlockingQueue<Block> blockingQueue) {
        this.blockingQueue = blockingQueue;
        subscribers = new LinkedList<>();
        initialize();
    }

    public void subscribe(DeferredResult<Block> result) {
        subscribers.add(result);
    }

    public Block getLastBlock() {
        return lastBlock;
    }

    public void removeSubscribe(DeferredResult<Block> result) {
        log.info("## Remove deferred, cur size : {}, index : {}", subscribers.size(), subscribers.indexOf(result));
        log.info("## Remove subscribers : " + subscribers.remove(result));
    }

    public void initialize() {
        log.info("## Initialize block consumer..");
        thread = new Thread(() -> {
            while (alive) {
                try {
                    Block block = blockingQueue.take();
                    lastBlock = block;
                    log.info("subscribe block.. subscribers : " + subscribers.size());
                    subscribers.forEach(subscriber -> subscriber.setResult(block));
                } catch (Exception e) {
                    alive = false;
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}