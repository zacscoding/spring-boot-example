package io.spring.batch.jobexample;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import lombok.extern.slf4j.Slf4j;

/**
 * {@link Job}이 실행되기 전과 후에 로그를 출력한다.
 */
@Slf4j
public class JobLoggerListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        logger.info("{} is beginning execution", jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        logger.info("{} has completed with the status {}",
                    jobExecution.getJobInstance().getJobName(),
                    jobExecution.getStatus());
    }
}
