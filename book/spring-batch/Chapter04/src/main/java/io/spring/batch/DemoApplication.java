package io.spring.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBatchProcessing
@SpringBootApplication
public class DemoApplication {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobExplorer jobExplorer;

    @Bean
    public Tasklet explorerTasklet() {
        return new ExploringTasklet(jobExplorer);
    }

    @Bean
    public Step explorerStep() {
        return stepBuilderFactory.get("explorerStep")
                                 .tasklet(explorerTasklet())
                                 .build();
    }

    @Bean
    public Job explorerJob() {
        return jobBuilderFactory.get("explorerJob")
                                .start(explorerStep())
                                .incrementer(new RunIdIncrementer())
                                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}