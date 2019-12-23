package demo.batch.scheduler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JobLauncherScheduler {
    private final Job job;
    private final JobLauncher jobLauncher;

    // @Scheduled(fixedRate = 30000L)
    @PostConstruct
    public void importTransactionScheduleTask() {
        try {
            jobLauncher.run(job, newExecution());
        } catch (JobInstanceAlreadyCompleteException e) {
            logger.warn("exception occur", e);
        } catch (JobExecutionAlreadyRunningException e) {
            logger.warn("exception occur", e);
        } catch (JobParametersInvalidException e) {
            logger.warn("exception occur", e);
        } catch (JobRestartException e) {
            logger.warn("exception occur", e);
        }
    }

    private JobParameters newExecution() {
        Map<String, JobParameter> parameters = new HashMap<>();

        JobParameter parameter = new JobParameter(new Date());
        parameters.put("currentTime", parameter);

        JobParameter parameter2 = new JobParameter(1L);
        parameters.put("clusterId", parameter2);

        return new JobParameters(parameters);
    }
}
