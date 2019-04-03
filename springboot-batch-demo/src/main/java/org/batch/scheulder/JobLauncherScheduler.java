package org.batch.scheulder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author zacconding
 * @Date 2018-08-04
 * @GitHub : https://github.com/zacscoding
 */
@Component
public class JobLauncherScheduler {

    @Autowired
    private Job job;
    @Autowired
    private JobLauncher jobLauncher;

    @Scheduled(fixedRate = 15000L)
    public void importUserScheduleTask() {
        try {
            jobLauncher.run(job, newExecution());
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        }
    }

    private void changeDataFile() {

    }

    private JobParameters newExecution() {
        Map<String, JobParameter> parameters = new HashMap<>();

        JobParameter parameter = new JobParameter(new Date());
        parameters.put("currentTime", parameter);

        return new JobParameters(parameters);
    }
}
