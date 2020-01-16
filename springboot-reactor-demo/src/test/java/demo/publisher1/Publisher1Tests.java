package demo.publisher1;

import static org.assertj.core.api.Assertions.assertThat;

import demo.dto.Pair;
import java.util.concurrent.CountDownLatch;
import org.junit.jupiter.api.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

/**
 *
 */
public class Publisher1Tests {

    CountDownLatch countDownLatch = new CountDownLatch(1);
    Long lastBlock;

    @Test
    public void runTests() throws Exception {
        BlockProcessor blockProcessor = new BlockProcessor();
        Disposable disposable = Flux.from(blockProcessor.blockStream())
            .subscribe(this::insertBlock, this::onError);

        Thread t = new Thread(new MockBlockchain(blockProcessor));
        t.setDaemon(true);
        t.start();

        countDownLatch.await();
        System.out.println("## after complete.. :: " + disposable.isDisposed());

        Flux.from(blockProcessor.blockStream())
            .subscribe(pair -> {
                    assertThat(pair.getFirst()).isEqualTo(lastBlock);
                }
            );
    }

    private void insertBlock(Pair<Long, String> newBlock) {
        System.out.println("## [Subscriber] Receive new block : " + newBlock);
        lastBlock = newBlock.getFirst();
        //ThreadUtil.printStackTrace();
        // countDownLatch.countDown();
    }

    private void onError(Throwable throwable) {
        System.err.println("exception occur : " + throwable.getMessage());
        countDownLatch.countDown();
    }

}
