package demo.reactor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Before;
import org.junit.Test;
import reactor.core.Disposable;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.FluxSink.OverflowStrategy;
import reactor.core.scheduler.Schedulers;

/**
 * http://woowabros.github.io/tools/2019/02/15/controller-log.html
 */
public class FluxTest {

    EmitterProcessor emitterProcessor;
    FluxSink<String> fluxSink;
    Flux<String> flux;

    @Before
    public void setUp() {
        this.emitterProcessor = EmitterProcessor.create();
        this.fluxSink = emitterProcessor.sink(OverflowStrategy.DROP);
        this.flux = emitterProcessor.publishOn(Schedulers.elastic());
    }

    @Test
    public void testFluxSink() throws Exception {
        Thread subscribeTask = new Thread(() -> {
            final SimpleDateFormat sdf = new SimpleDateFormat("[HH:mm:ss,SSS]");
            flux.bufferTimeout(10, Duration.ofSeconds(3), Schedulers.elastic())
                .filter(values -> values != null && !values.isEmpty())
                .subscribe(values -> {
                    String join = values.stream().collect(Collectors.joining(" "));
                    System.out.printf("Subscribe sink [%s](%d) :: %s\n", sdf.format(new Date()), values.size(), join);
                });
        });
        subscribeTask.setDaemon(true);
        subscribeTask.start();

        Thread produceTask = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    int size = new Random().nextInt(15) + 1;
                    for (int i = 0; i < size; i++) {
                        fluxSink.next("id" + i);
                    }
                    TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        produceTask.setDaemon(true);
        produceTask.start();

        TimeUnit.MINUTES.sleep(3L);
    }
}
