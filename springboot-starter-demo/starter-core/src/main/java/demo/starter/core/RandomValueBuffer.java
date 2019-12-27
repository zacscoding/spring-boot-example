package demo.starter.core;

import java.time.Duration;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.FluxSink.OverflowStrategy;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class RandomValueBuffer {

    // required
    private final Optional<RandomValueListener> listenerOptional;

    // build
    private final FluxSink<String> idFluxSink;
    private final Flux<String> idFlux;

    // after started
    private Disposable subscription;

    public RandomValueBuffer(RandomValueListener listener) {
        listenerOptional = Optional.ofNullable(listener);

        final EmitterProcessor<String> emitterProcessor = EmitterProcessor.create();
        idFluxSink = emitterProcessor.sink(OverflowStrategy.BUFFER);
        idFlux = emitterProcessor.publishOn(Schedulers.elastic());
    }

    public void start(int maxSize, Duration maxTime) {
        if (subscription != null) {
            logger.warn("Already RandomValueBuffer is started");
            return;
        }

        logger.info("Start random value buffer with max size : {} / max time : {}", maxSize, maxTime);

        subscription = idFlux.bufferTimeout(maxSize, maxTime, Schedulers.elastic())
                             .subscribe(values -> {
                                 System.out.println(">> Flush random id buffer");
                                 System.out.println(String.join("\n", values));
                                 listenerOptional.ifPresent(listener -> listener.onValues(values));
                             });
    }

    public void stop() {
        if (subscription != null) {
            subscription.dispose();
            subscription = null;
            logger.debug("Success to stop RandomValueBuffer");
        }
    }

    public void publishRandomId(String id) {
        idFluxSink.next(id);
    }
}
