package org.async.web;

import com.sun.media.jfxmedia.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.async.consumer.BlockConsumer;
import org.async.model.Block;
import org.async.producer.BlockProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author zacconding
 * @Date 2018-04-22
 * @GitHub : https://github.com/zacscoding
 */
@Slf4j
@Controller
public class AsyncController {

    @Autowired
    private BlockConsumer blockConsumer;

    @GetMapping("/index")
    public String test() {
        return "index";
    }

    @GetMapping("/last-block")
    public ResponseEntity<Block> getLastBlock() {
        log.info("# Request last block.. : " + blockConsumer.getLastBlock());
        return ResponseEntity.ok().body(blockConsumer.getLastBlock());
    }

    @GetMapping("/subscribe")
    @ResponseBody
    public DeferredResult<Block> getBlock() {
        final DeferredResult<Block> deferredResult = new DeferredResult<>(null);
        blockConsumer.subscribe(deferredResult);

        deferredResult.onCompletion(() -> blockConsumer.removeSubscribe(deferredResult));
        deferredResult.onError((throwable) -> blockConsumer.removeSubscribe(deferredResult));
        deferredResult.onTimeout(() -> blockConsumer.removeSubscribe(deferredResult));

        return deferredResult;
    }
}
