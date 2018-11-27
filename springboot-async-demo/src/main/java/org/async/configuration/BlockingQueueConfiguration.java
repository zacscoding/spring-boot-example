package org.async.configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.async.model.Block;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zacconding
 * @Date 2018-04-22
 * @GitHub : https://github.com/zacscoding
 */
@Configuration
public class BlockingQueueConfiguration {

    @Bean
    public BlockingQueue<Block> blockingQueue() {
        return new LinkedBlockingQueue();
    }
}
