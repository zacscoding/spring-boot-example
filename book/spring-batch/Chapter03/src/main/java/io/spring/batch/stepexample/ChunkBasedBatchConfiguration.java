package io.spring.batch.stepexample;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.policy.CompositeCompletionPolicy;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.batch.repeat.policy.TimeoutTerminationPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("step-example-chunk")
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class ChunkBasedBatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job chunkBasedJob() {
        return this.jobBuilderFactory.get("chunkBasedJob")
                                     .start(chunkStep())
                                     .build();
    }

    @Bean
    public Step chunkStep() {
        return this.stepBuilderFactory.get("chunkStep")
                                      //.<String, String>chunk(5)
                                      .<String, String>chunk(completionPolicy())
                                      .reader(itemReader())
                                      .writer(itemWriter())
                                      .build();
    }

    @Bean
    public ListItemReader<String> itemReader() {
        return new ListItemReader<>(
                IntStream.range(0, 1000).boxed().map(i -> UUID.randomUUID().toString()).collect(Collectors.toList())
        );
    }

    @Bean
    public ItemWriter<String> itemWriter() {
        return items -> items.forEach(i -> logger.info(">> Current write item: {}", i));
    }

    @Bean
    public CompletionPolicy completionPolicy() {
        final CompositeCompletionPolicy policy = new CompositeCompletionPolicy();

        policy.setPolicies(new CompletionPolicy[] {
                new TimeoutTerminationPolicy(3),
                new SimpleCompletionPolicy(10)
        });

        return policy;
    }
}
