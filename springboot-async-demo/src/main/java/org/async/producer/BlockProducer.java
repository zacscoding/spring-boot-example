package org.async.producer;

import java.util.concurrent.BlockingQueue;
import lombok.extern.slf4j.Slf4j;
import org.async.model.Block;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-04-22
 * @GitHub : https://github.com/zacscoding
 */

@Slf4j
@Component
public class BlockProducer {

    private Thread thread;
    private boolean alive = true;
    private int blockNumber = 0;
    private BlockingQueue<Block> blockingQueue;

    @Autowired
    public BlockProducer(BlockingQueue<Block> blockingQueue) {
        this.blockingQueue = blockingQueue;
        initialize();
    }

    public Block getLastBlock() {
        return blockingQueue.isEmpty() ? null : blockingQueue.poll();
    }

    private void initialize() {
        log.info("## Initialize block producer..");
        thread = new Thread(() -> {
            while (alive) {
                try {
                    log.info("## Create block..");
                    int blockNum = blockNumber++;
                    blockingQueue.add(Block.builder().blockNumber(blockNum).name("block" + blockNum).build());
                    Thread.sleep(5000L);
                } catch (Exception e) {
                    log.error("PRODUCE ERROR", e);
                    alive = false;
                }
            }
        });

        thread.setDaemon(true);
        thread.start();
    }
}
