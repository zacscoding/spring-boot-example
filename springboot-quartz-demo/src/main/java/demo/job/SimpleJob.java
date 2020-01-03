package demo.job;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.extern.slf4j.Slf4j;

/**
 * https://advenoh.tistory.com/52
 */
@Slf4j
public class SimpleJob extends QuartzJobBean {

    private int MAX_SLEEP_IN_SECONDS = 5;
    private volatile Thread currThread;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobKey jobKey = context.getJobDetail().getKey();
        currThread = Thread.currentThread();
        IntStream.range(0, 5).forEach(i -> {
            logger.info("SimpleJob Counting - {}", i);
            try {
                TimeUnit.SECONDS.sleep(MAX_SLEEP_IN_SECONDS);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        });
    }
}
