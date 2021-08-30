package io.spring.batch.stepexample;

import java.util.Random;
import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("step-example-chunk-policy")
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class ChunkPolicyBatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job chunkPolicyJob() {
        return this.jobBuilderFactory.get("chunkPolicyJob")
                                     .start(chunkPolicyStep())
                                     .incrementer(new RunIdIncrementer())
                                     .build();
    }

    @Bean
    public Step chunkPolicyStep() {
        return this.stepBuilderFactory.get("chunkPolicyStep")
                                      .<String, String>chunk(randomCompletionPolicy())
                                      .reader(new BoundedItemReader())
                                      .writer(itemWriter())
                                      .build();
    }

    @Bean
    public ItemWriter<String> itemWriter() {
        return items -> logger.info("## [Writer] write items: #{}", items.size());
    }

    @Bean
    public CompletionPolicy randomCompletionPolicy() {
        return new RandomChunkSizePolicy();
    }

    public static class BoundedItemReader implements ItemReader<String> {

        private int totalReadItems;

        @Override
        public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
            if (totalReadItems > 20) {
                return null;
            }
            final String item = UUID.randomUUID().toString();
            logger.info("## [Reader] read item: {}", item);
            totalReadItems++;
            return item;
        }
    }

    @Slf4j
    public static class RandomChunkSizePolicy implements CompletionPolicy {

        private int chunkSize;
        private int totalProcessed;
        private Random random = new Random();

        @Override
        public boolean isComplete(RepeatContext context, RepeatStatus result) {
            if (RepeatStatus.FINISHED == result) {
                return true;
            }
            return isComplete(context);
        }

        @Override
        public boolean isComplete(RepeatContext context) {
            return totalProcessed >= chunkSize;
        }

        @Override
        public RepeatContext start(RepeatContext parent) {
            chunkSize = random.nextInt(5);
            totalProcessed = 0;
            logger.info("The chunk size has been set to {}", chunkSize);
            return parent;
        }

        @Override
        public void update(RepeatContext context) {
            this.totalProcessed++;
        }
    }

}
