package io.spring.batch.jobexample;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AnnotationJobLoggerListener {

    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        logger.info("{} is beginning execution", jobExecution.getJobInstance().getJobName());
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        logger.info("{} has completed with the status {}",
                    jobExecution.getJobInstance().getJobName(),
                    jobExecution.getStatus());
    }
}
