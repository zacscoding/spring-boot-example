package io.spring.batch.database.example7;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort.Direction;

import ch.qos.logback.classic.Level;
import io.spring.batch.database.listener.LoggingChunkListener;
import io.spring.batch.util.LogLevelUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBatchProcessing
@SpringBootApplication
@RequiredArgsConstructor
public class RepositoryItemReaderMain {

    private static final int CHUNK_SIZE = 3;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public static void main(String[] args) {
        args = new String[] { "city=Dover" };
        SpringApplication.run(RepositoryItemReaderMain.class, args);
    }

    @PostConstruct
    private void setUp() {
        LogLevelUtil.setLevel("p6spy", Level.INFO);
    }

    @Bean
    public Job copyCustomerJob() {
        return jobBuilderFactory.get("copyCustomerJob")
                                .incrementer(new RunIdIncrementer())
                                .start(copyCustomerStep())
                                .listener(new JobExecutionListener() {
                                    @Override
                                    public void beforeJob(JobExecution jobExecution) {
                                        //LogLevelUtil.setLevel("p6spy", Level.INFO);
                                    }

                                    @Override
                                    public void afterJob(JobExecution jobExecution) {}
                                })
                                .build();
    }

    @Bean
    public Step copyCustomerStep() {
        return stepBuilderFactory.get("copyCustomerStep")
                                 .<Customer, Customer>chunk(CHUNK_SIZE)
                                 // .reader(customerItemReader(null, null)) // use JpaPagingItemReaderBuilder
                                 .reader(customerItemReader(null, null)) // custom query provider
                                 .writer(customerItemWriter())
                                 .listener(new LoggingChunkListener())
                                 .build();
    }

    @Bean
    public ItemWriter<Customer> customerItemWriter() {
        final AtomicInteger attempts = new AtomicInteger(0);
        final AtomicInteger itemCount = new AtomicInteger(0);
        return items -> {
            logger.info("Write Chunk-{} items: #{}", attempts.incrementAndGet(), items.size());
            items.forEach(item -> logger.info("Current customer: {}. Total Item Write: #{}", item,
                                              itemCount.incrementAndGet()));
        };
    }

    @Bean
    @StepScope
    public RepositoryItemReader<Customer> customerItemReader(
            CustomerRepository repository,
            @Value("#{jobParameters['city']}") String city) {

        return new RepositoryItemReaderBuilder<Customer>()
                .name("customertemReader")
                .arguments(Collections.singletonList(city))
                .methodName("findByCity")
                .repository(repository)
                .sorts(Collections.singletonMap("lastName", Direction.ASC))
                .pageSize(CHUNK_SIZE)
                .build();
        /*
        -- select query
        select
            customer0_.id as id1_0_, ...
        from
            customer customer0_
        where customer0_.city='Dover' order by customer0_.lastName asc limit 10;

        -- count query
        select count(customer0_.id) as col_0_0_ from customer customer0_ where customer0_.city='Dover';
         */
    }
}

