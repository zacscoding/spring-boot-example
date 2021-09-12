package io.spring.batch.flowexample;

import java.util.Random;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

import io.spring.batch.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RandomDecider implements JobExecutionDecider {

    private final Random random = new Random();

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        final boolean isSuccess = random.nextBoolean();
        logger.info("StackTrace\n{}", ThreadUtil.getStackTrace());
        logger.info("decide job: {}, step: {} > {}",
                    jobExecution.getJobInstance().getJobName(), stepExecution.getStepName(),
                    isSuccess ? FlowExecutionStatus.COMPLETED.getName() : FlowExecutionStatus.FAILED.getName());

        return isSuccess ? new FlowExecutionStatus(FlowExecutionStatus.COMPLETED.getName())
                         : new FlowExecutionStatus(FlowExecutionStatus.FAILED.getName());
    }
}
