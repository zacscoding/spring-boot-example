package demo.collector;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.FluxSink.OverflowStrategy;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class AccessLogCollector {

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final int maxSize = 5;
    private final Duration maxTime = Duration.ofSeconds(3L);
    private final AtomicInteger requestedCount = new AtomicInteger(0);
    private final Path logPath = Paths.get("log");

    private final FluxSink<String> accessLogFluxSink;
    private final Flux<String> accessLogFlux;
    private Disposable subscription;

    public AccessLogCollector() {
        final EmitterProcessor<String> emitterProcessor = EmitterProcessor.create();
        accessLogFluxSink = emitterProcessor.sink(OverflowStrategy.BUFFER);
        accessLogFlux = emitterProcessor.publishOn(Schedulers.elastic());
    }

    public int getRequestedCount() {
        return requestedCount.get();
    }

    /**
     * Push a given access log
     */
    public void pushAccessLog(String accessLog) {
        if (!running.get()) {
            throw new IllegalStateException("Must started access log collector before putting log");
        }

        requestedCount.getAndIncrement();
        accessLogFluxSink.next(accessLog);
    }

    /**
     * Start a access log collector
     */
    public void start() {
        if (!running.compareAndSet(false, true)) {
            logger.warn("Already access log collector is started");
            return;
        }

        try {
            Files.createDirectories(logPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        subscription = accessLogFlux.bufferTimeout(maxSize, maxTime, Schedulers.elastic())
                                    .subscribe(this::flush);
        logger.info("Success to start access log collector");
    }

    /**
     * Stop a access log collector
     */
    public void stop() {
        if (!running.compareAndSet(true, false)) {
            logger.warn("Already access log collector is stopped");
            return;
        }

        if (subscription != null) {
            subscription.dispose();
            subscription = null;
        }

        logger.info("Success to stop access log collector");
    }

    private void flush(List<String> accessLogs) {
        if (accessLogs.isEmpty()) {
            return;
        }

        String fileName = String.format("access-%s.log",
                                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH-mm-ss")));

        File accessLogFile = logPath.resolve(fileName).toFile();

        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(accessLogFile, true)))) {
            for (String accessLog : accessLogs) {
                pw.print(accessLog);
                pw.print('\n');
            }
        } catch (IOException e) {
            logger.warn("Exception occur while writing access log", e);
        }
    }
}
