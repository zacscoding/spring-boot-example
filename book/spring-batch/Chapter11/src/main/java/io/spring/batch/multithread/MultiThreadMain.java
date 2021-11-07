package io.spring.batch.multithread;

import javax.sql.DataSource;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBatchProcessing
@SpringBootApplication
public class MultiThreadMain {

    public static void main(String[] args) {
        args = new String[] {
                "inputFlatFile=input/transaction1.csv",
                };
        SpringApplication.run(MultiThreadMain.class, args);
    }

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    @StepScope
    public FlatFileItemReader<Transaction> fileTransactionReader(
            @Value("#{jobParameters['inputFlatFile']}") Resource inputFile) {

        return new FlatFileItemReaderBuilder<Transaction>()
                .name("transactionItemReader")
                .resource(inputFile)
                .saveState(false)
                .delimited()
                .names(new String[] { "account", "amount", "timestamp" })
                .fieldSetMapper(fieldSet -> {
                    Transaction transaction = new Transaction();

                    transaction.setAccount(fieldSet.readString("account"));
                    transaction.setAmount(fieldSet.readBigDecimal("amount"));
                    transaction.setTimestamp(fieldSet.readDate("timestamp", "yyyy-MM-dd HH:mm:ss"));

                    return transaction;
                })
                .build();
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<Transaction> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Transaction>()
                .dataSource(dataSource)
                .sql("INSERT INTO transaction_simple (account, amount, timestamp) "
                     + "VALUES(:account, :amount, :timestamp)")
                .beanMapped()
                .build();
    }

    @Bean
    public Job multiThreadJob() {
        return jobBuilderFactory.get("multiThreadJob")
                                .incrementer(new RunIdIncrementer())
                                .start(step1())
                                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                                 .<Transaction, Transaction>chunk(100)
                                 .reader(fileTransactionReader(null))
                                 .writer(writer(null))
                                 .taskExecutor(new SimpleAsyncTaskExecutor())
                                 .listener(new ChunkListener() {
                                     @Override
                                     public void beforeChunk(ChunkContext context) {
                                         logger.info("beforeChunk: {}", context.getStepContext().getId());
                                     }

                                     @Override
                                     public void afterChunk(ChunkContext context) {
                                         logger.info("afterChunk: {}", context.getStepContext().getId());
                                     }

                                     @Override
                                     public void afterChunkError(ChunkContext context) {
                                         logger.info("afterChunkError: {}", context.getStepContext().getId());
                                     }
                                 })
                                 .build();
    }

}
