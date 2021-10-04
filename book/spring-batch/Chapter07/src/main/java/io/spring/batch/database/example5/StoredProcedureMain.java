package io.spring.batch.database.example5;

import java.sql.Types;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.StoredProcedureItemReader;
import org.springframework.batch.item.database.builder.StoredProcedureItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.SqlParameter;

import ch.qos.logback.classic.Level;
import io.spring.batch.database.domain.Customer;
import io.spring.batch.database.domain.CustomerRowMapper;
import io.spring.batch.database.listener.LoggingChunkListener;
import io.spring.batch.util.LogLevelUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBatchProcessing
@SpringBootApplication
@RequiredArgsConstructor
public class StoredProcedureMain {

    private static final int CHUNK_SIZE = 3;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public static void main(String[] args) {
        args = new String[] { "city=Dover" };
        SpringApplication.run(StoredProcedureMain.class, args);
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
                                .build();
    }

    @Bean
    public Step copyCustomerStep() {
        return stepBuilderFactory.get("copyCustomerStep")
                                 .<Customer, Customer>chunk(CHUNK_SIZE)
                                 .reader(customerItemReader(null, null))
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
    public StoredProcedureItemReader<Customer> customerItemReader(DataSource dataSource,
                                                                  @Value("#{jobParameters['city']}") String city) {

        return new StoredProcedureItemReaderBuilder<Customer>()
                .name("customerItemReader")
                .dataSource(dataSource)
                .procedureName("customer_list")
                .parameters(new SqlParameter[] {
                        new SqlParameter("cityOption", Types.VARCHAR)
                })
                .preparedStatementSetter(
                        new ArgumentPreparedStatementSetter(new Object[] { city })
                )
                .rowMapper(new CustomerRowMapper())
                .build();
    }

}
