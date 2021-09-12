package io.spring.batch.chunkexample;

import java.util.List;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile({ "chunk-example-static" })
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class StaticChunkConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job.chunk")
                                .incrementer(new RunIdIncrementer())
                                .start(step1())
                                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step.chunk.static")
                                 .<String, String>chunk(10)
                                 .reader(new SimpleItemReader(22))
                                 .writer(new SimpleItemWriter())
                                 .listener(new LoggingStepExecutionListener())
                                 .listener(new LoggingChunkListener())
                                 .build();
    }

    public static class SimpleItemReader implements ItemReader<String> {

        private final int max;
        private int readCount;

        public SimpleItemReader(int max) {
            this.max = max;
        }

        @Override
        public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
            if (readCount > max) {
                return null;
            }

            String item = String.format("item-%d", readCount);
            logger.info("read item: {}", item);
            readCount++;

            return item;
        }
    }

    public static class SimpleItemWriter implements ItemWriter<String> {

        @Override
        public void write(List<? extends String> items) throws Exception {
            logger.info("[Writer] write items: {}. start: {} / end: {}", items.size(),
                        items.get(0), items.get(items.size() - 1));
        }
    }

    public static class LoggingStepExecutionListener implements StepExecutionListener {

        @Override
        public void beforeStep(StepExecution stepExecution) {
            logger.info("[LoggingStepExecutionListener] beforeStep.. {}", stepExecution);
        }

        @Override
        public ExitStatus afterStep(StepExecution stepExecution) {
            logger.info("[LoggingStepExecutionListener] afterStep.. {}", stepExecution);
            return stepExecution.getExitStatus();
        }
    }

    public static class LoggingChunkListener implements ChunkListener {

        @Override
        public void beforeChunk(ChunkContext context) {
            logger.info("[LoggingChunkListener] beforeChunk.. {}", context);
        }

        @Override
        public void afterChunk(ChunkContext context) {
            logger.info("[LoggingChunkListener] afterChunk.. {}", context);
        }

        @Override
        public void afterChunkError(ChunkContext context) {
            logger.info("[LoggingChunkListener] afterChunkError.. {}", context);
        }
    }
}

