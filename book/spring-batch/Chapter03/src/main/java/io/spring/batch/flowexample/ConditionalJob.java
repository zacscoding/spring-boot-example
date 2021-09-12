package io.spring.batch.flowexample;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("conditional-job-example")
@EnableBatchProcessing
@Configuration
@RequiredArgsConstructor
public class ConditionalJob {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Tasklet passTasklet() {
        return (contribution, chunkContext) -> {
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet successTasklet() {
        return (contribution, chunkContext) -> {
            logger.info("Success!");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet failTasklet() {
        return (contribution, chunkContext) -> {
            logger.info("Failure!");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("conditionalJob")
                                .incrementer(new RunIdIncrementer())
                                .start(firstStep())
                                .on("FAILED").to(failureStep())
                                .from(firstStep()).on("*").to(successStep())
                                .end()
                                .build();
    }

    @Bean
    public Step firstStep() {
        return stepBuilderFactory.get("firstStep")
                                 .tasklet(passTasklet())
                                 .build();
    }

    @Bean
    public Step successStep() {
        return stepBuilderFactory.get("successStep")
                                 .tasklet(successTasklet())
                                 .build();
    }

    @Bean
    public Step failureStep() {
        return stepBuilderFactory.get("failureStep")
                                 .tasklet(failTasklet())
                                 .build();
    }
}
