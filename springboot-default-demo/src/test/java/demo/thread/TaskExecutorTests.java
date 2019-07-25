package demo.thread;

import java.util.Random;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 *
 */
@Slf4j
public class TaskExecutorTests {

    private AtomicInteger atomicInteger = new AtomicInteger(1);

    @Test
    public void testFutures() throws Exception {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.initialize();
        executor.setDaemon(true);

        for (int i = 0; i < 10; i++) {
            /*ListenableFuture<String> future = executor.submitListenable(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    if (new Random().nextInt(100) % 2 == 0) {
                        throw new Exception("Forces Exception");
                    }

                    return "Success : " + atomicInteger.getAndIncrement();
                }
            });*/

            Future<?> future = executor.submit(() -> {
                if (new Random().nextInt(100) % 2 == 0) {
                    throw new Exception("Forces Exception");
                }

                return "Success : " + atomicInteger.getAndIncrement();
            });

            try {
                logger.info("Success to get() : {}", future.get());
            } catch (ExecutionException e) {
                logger.warn("Failed to get(). reason : {}", e.getCause().getMessage());
            }
        }
    }

    @Test
    public void testSubmitAndCheckAliveThreadOrNot() throws Exception {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.initialize();
        executor.setDaemon(true);

        Future<String> future = executor.submit(() -> {
            final String prefix = "[Callable]";
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    logger.info("{} : Working.... : {}", prefix, atomicInteger.getAndIncrement());
                    TimeUnit.MILLISECONDS.sleep(500L);
                }

                return "Success";
            } catch (Exception e) {
                logger.error("{} : Exception occur in thread. {}", prefix, e.getMessage());
                throw e;
            } finally {
                logger.info("{} : complete", prefix);
            }

        });

        try {
            logger.info("Check future.isDone() :: {}", future.isDone());
            //logger.info("Check future.cancel(true) :: {}", future.cancel(true));
            String result = future.get(20L, TimeUnit.SECONDS);
            logger.info("Success to get() : {}", result);
        } catch (ExecutionException e) {
            logger.warn("Exception occur while getting futures...", e);
        } catch (TimeoutException e) {
            logger.warn("Exception occur while getting futures...", e);
            future.cancel(true);
        } catch (CancellationException e) {
            logger.warn("Exception occur while canceling future..", e);
        }

        logger.info(">> Start to wait 3 seconds");
        TimeUnit.SECONDS.sleep(3L);
    }

}
