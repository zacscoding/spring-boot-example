package demo.publisher1;

import demo.dto.Pair;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 */
public class MockBlockchain implements Runnable {

    private AtomicLong blockNumber = new AtomicLong(0L);
    private BlockProcessor blockProcessor;

    public MockBlockchain(BlockProcessor blockProcessor) {
        this.blockProcessor = blockProcessor;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Long bestBlockNumber = blockNumber.getAndIncrement();

                if (bestBlockNumber > 5) {
                    blockProcessor.error(new Exception("Force exception"));
                } else {
                    Pair<Long, String> newBlock = Pair.newInstance(bestBlockNumber, UUID.randomUUID().toString());
                    blockProcessor.newBlock(newBlock);
                }

                TimeUnit.SECONDS.sleep(2L);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
