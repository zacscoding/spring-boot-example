package io.spring.batch.taskletexample;

import java.util.concurrent.Callable;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.CallableTaskletAdapter;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import io.spring.batch.util.ThreadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile({ "tasklet-example" })
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final Environment environment;
    private int processed = 0;

    @Bean
    public Job job() {
        return this.jobBuilderFactory.get("job")
                                     .start(step())
                                     .incrementer(new RunIdIncrementer())
                                     .build();
    }

    @Bean
    public Step step() {
        final String profiles = String.join(" ", environment.getActiveProfiles());

        if (profiles.contains("repeatable")) {
            return repeatableStep();
        }

        if (profiles.contains("stack-trace-logging")) {
            return stackTraceLoggingStep();
        }

        if (profiles.contains("callable-adapter")) {
            return callableTaskletAdapter();
        }

        throw new IllegalStateException(
                String.format("could not find pre-defined steps. active profiles: %s", profiles));
    }

    private Step repeatableStep() {
        final Tasklet tasklet = (contribution, chunkContext) -> {
            BatchConfiguration.this.processed++;
            logger.info("Running tasklet.. processed:{}", BatchConfiguration.this.processed);

            if (BatchConfiguration.this.processed <= 5) {
                return RepeatStatus.CONTINUABLE;
            }

            try {
                final TransactionStatus transactionStatus = TransactionAspectSupport.currentTransactionStatus();
                logger.info("Current transaction status: {}", transactionStatus);
            } catch (NoTransactionException e) {
                logger.error("No transaction in current thread: {}", e.getMessage());
            }
            logger.info("Current stack trace\n{}", ThreadUtil.getStackTrace());

            logger.info("Will return RepeatStatus.FINISHED");
            return RepeatStatus.FINISHED;
        };

        return this.stepBuilderFactory.get("repeatableStep")
                                      .tasklet(tasklet)
                                      .build();
    }

    private Step stackTraceLoggingStep() {
        return this.stepBuilderFactory.get("stackTraceLoggingStep")
                                      .tasklet(((contribution, chunkContext) -> {
                                          logger.info("Current stack trace\n{}", ThreadUtil.getStackTrace());
                                          return RepeatStatus.FINISHED;
                                      }))
                                      .build();
    }

    private Step callableTaskletAdapter() {
        final CallableTaskletAdapter callableTaskletAdapter = new CallableTaskletAdapter();

        callableTaskletAdapter.setCallable(callableObject());

        return this.stepBuilderFactory.get("callableAdapter").
                                      tasklet(callableTaskletAdapter).
                                      build();
    }

    @Bean
    public Callable<RepeatStatus> callableObject() {
        return () -> {
            logger.info("This was executed in another thread");
            logger.info("Current stack trace\n{}", ThreadUtil.getStackTrace());
            return RepeatStatus.FINISHED;
        };
    }
}
