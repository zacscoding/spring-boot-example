package demo.job;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.extern.slf4j.Slf4j;

/**
 * https://advenoh.tistory.com/52
 */
@Slf4j
public class CronJob extends QuartzJobBean {

    private int MAX_SLEEP_IN_SECONDS = 5;
    private volatile Thread currThread;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        if (jobDataMap.size() > 0) {
            int jobId = jobDataMap.getInt("jobId");
            JobKey jobKey = context.getJobDetail().getKey();

            currThread = Thread.currentThread();
            logger.info("============================================================================");
            logger.info("CronJob started :: sleep : {} jobId : {} jobKey : {} - {}", MAX_SLEEP_IN_SECONDS,
                        jobId,
                        jobKey, currThread.getName());

            IntStream.range(0, 3).forEach(i -> {
                logger.info("CronJob Counting - {}", i);
                try {
                    TimeUnit.SECONDS.sleep(MAX_SLEEP_IN_SECONDS);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            });
            logger.info("CronJob ended :: jobKey : {} - {}", jobKey, currThread.getName());
            logger.info("============================================================================");
        }
    }
}