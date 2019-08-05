package demo.publisher1;

import demo.dto.Pair;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.ReplayProcessor;

@Slf4j(topic = "publisher1.BlockProcessor")
public class BlockProcessor {

    // replay only last item
    private final ReplayProcessor<Pair<Long, String>> blockProcessor = ReplayProcessor.cacheLast();
    private final FluxSink<Pair<Long, String>> blockSink = blockProcessor.sink();

    public Publisher<Pair<Long, String>> blockStream() {
        return blockProcessor;
    }

    public void newBlock(Pair<Long, String> block) {
        logger.info("receive new block : {}", block);
        blockSink.next(block);
    }

    public void error(Throwable throwable) {
        blockSink.error(throwable);
    }
}
