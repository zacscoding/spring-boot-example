package io.spring.batch;

import java.util.List;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExploringTasklet implements Tasklet {

    private final JobExplorer explorer;

    public ExploringTasklet(JobExplorer explorer) {
        this.explorer = explorer;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        final String jobName = chunkContext.getStepContext().getJobName();
        final List<JobInstance> instances = explorer.getJobInstances(jobName, 0, Integer.MAX_VALUE);

        logger.info("## There are {} job instances for the job {}", instances.size(), jobName);
        logger.info("They have had the following results");
        logger.info("****************************************************");
        for (JobInstance instance : instances) {
            final List<JobExecution> executions = explorer.getJobExecutions(instance);
            logger.info("> Instance {} had {} executions", instance.getInstanceId(), executions.size());

            for (JobExecution execution : executions) {
                logger.info("\tExecution {} resulted in ExitStatus {}", execution.getId(), execution.getExitStatus());
            }
        }

        return RepeatStatus.FINISHED;
    }
}
