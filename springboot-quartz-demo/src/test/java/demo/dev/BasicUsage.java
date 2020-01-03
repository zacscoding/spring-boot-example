package demo.dev;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.junit.jupiter.api.Test;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class BasicUsage {

    private static final Logger logger = LoggerFactory.getLogger(BasicUsage.class);

    @Test
    public void runSayHello() throws Exception {
        JobDetail jobDetail = newJob(SayHelloJob.class).build();
        Trigger trigger = newTrigger().build();

        Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
        defaultScheduler.start();
        defaultScheduler.scheduleJob(jobDetail, trigger);
        Thread.sleep(3 * 1000);

        // 스케줄러 종료
        defaultScheduler.shutdown(true);

    }

    public static class SayHelloJob implements Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            logger.info("## execute a job.. {}", context);
        }
    }
}
