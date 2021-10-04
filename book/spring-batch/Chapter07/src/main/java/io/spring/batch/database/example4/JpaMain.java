package io.spring.batch.database.example4;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

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
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.orm.AbstractJpaQueryProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;

import ch.qos.logback.classic.Level;
import io.spring.batch.database.listener.LoggingChunkListener;
import io.spring.batch.util.LogLevelUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBatchProcessing
@SpringBootApplication
@RequiredArgsConstructor
public class JpaMain {

    private static final int CHUNK_SIZE = 3;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public static void main(String[] args) {
        args = new String[] { "city=Dover" };
        SpringApplication.run(JpaMain.class, args);
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
                                 //.reader(customerItemReaderByCursor(null, null))
                                 // .reader(customerItemReader(null, null)) // use JpaPagingItemReaderBuilder
                                 .reader(createItemReaderByQueryProvider(null, null)) // custom query provider
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
    public JpaCursorItemReader<Customer> customerItemReaderByCursor(
            EntityManagerFactory entityManagerFactory,
            @Value("#{jobParameters['city']}") String city) {

        return new JpaCursorItemReaderBuilder<Customer>()
                .name("customerItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT c FROM Customer c where c.city = :city")
                //.queryProvider()
                .parameterValues(Collections.singletonMap("city", city))
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Customer> customerItemReader(
            EntityManagerFactory entityManagerFactory,
            @Value("#{jobParameters['city']}") String city) {

        return new JpaPagingItemReaderBuilder<Customer>()
                .name("customerItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT c from Customer c where c.city = :city")
                .parameterValues(Collections.singletonMap("city", city))
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Customer> createItemReaderByQueryProvider(
            EntityManagerFactory entityManagerFactory,
            @Value("#{jobParameters['city']}") String city) {
        final CustomerByCityQueryProvider queryProvider = new CustomerByCityQueryProvider(city);

        return new JpaPagingItemReaderBuilder<Customer>()
                .name("customerItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryProvider(queryProvider)
                .parameterValues(Collections.singletonMap("city", city))
                .pageSize(CHUNK_SIZE)
                .build();
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerByCityQueryProvider extends AbstractJpaQueryProvider {

        private String cityName;

        public Query createQuery() {
            final EntityManager em = getEntityManager();
            return em.createQuery("SELECT c FROM Customer c WHERE c.city = :city")
                     .setParameter("city", cityName);
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            Assert.notNull(cityName, "City name is required");
        }
    }
}

