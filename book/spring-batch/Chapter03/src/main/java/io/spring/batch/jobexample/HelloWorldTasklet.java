package io.spring.batch.jobexample;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import lombok.extern.slf4j.Slf4j;

/**
 * {@link JobParameters}로 전달된 "name" 값을 {@link ExecutionContext}에 {"user.name": name}으로 추가한다.
 */
@Slf4j
public class HelloWorldTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {

        final String name = (String) context.getStepContext()
                                            .getJobParameters()
                                            .get("name");

        // context.getStepContext().getJobExecutionContext(); 를 통해 ExecutionContext 조회가 가능하지만,
        // Copy + Immutable Map 이므로 데이터 변경이 불가능하다.
        final ExecutionContext jobContext = context.getStepContext()
                                                   .getStepExecution()
                                                   .getJobExecution()
                                                   .getExecutionContext();
        jobContext.put("user.name", name);

        logger.info("Hello, {}", name);
        return RepeatStatus.FINISHED;
    }
}
