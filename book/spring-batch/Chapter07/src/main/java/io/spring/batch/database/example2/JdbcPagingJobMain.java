package io.spring.batch.database.example2;

import java.util.Map;
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
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.google.common.collect.ImmutableMap;

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
public class JdbcPagingJobMain {

    private static final int CHUNK_SIZE = 3;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public static void main(String[] args) {
        args = new String[] { "city=Dover" };
        SpringApplication.run(JdbcPagingJobMain.class, args);
    }

    @PostConstruct
    private void setUp() {
        LogLevelUtil.setLevel("org.springframework.batch", Level.TRACE);
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
                                 .reader(customerItemReader(null, null, null))
                                 .writer(customerItemWriter())
                                 .listener(new LoggingChunkListener())
                                 .build();
    }

    @Bean
    public ItemWriter<Customer> customerItemWriter() {
        final AtomicInteger attempts = new AtomicInteger(0);
        return items -> {
            logger.info("Write Chunk-{} items: #{}", attempts.incrementAndGet(), items.size());
            items.forEach(item -> logger.info("Current customer: {}", item));
        };
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<Customer> customerItemReader(DataSource dataSource,
                                                             PagingQueryProvider queryProvider,
                                                             @Value("#{jobParameters['city']}") String city) {
        final Map<String, Object> parameterValues = ImmutableMap.of("city", city);
        return new JdbcPagingItemReaderBuilder<Customer>()
                .name("customerItemReader")
                .dataSource(dataSource)
                .queryProvider(queryProvider)
                .parameterValues(parameterValues)
                .pageSize(3)
                .rowMapper(new CustomerRowMapper())
                .build();
    }

    @Bean
    public SqlPagingQueryProviderFactoryBean pagingQueryProvider(DataSource dataSource) {
        final SqlPagingQueryProviderFactoryBean factoryBean = new SqlPagingQueryProviderFactoryBean();

        factoryBean.setSelectClause("SELECT *");
        factoryBean.setFromClause("from customer");
        factoryBean.setWhereClause("where city = :city");
        factoryBean.setSortKey("lastName");
        factoryBean.setDataSource(dataSource);

        return factoryBean;
    }
}
