package io.spring.batch.database.example3;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;

import org.hibernate.SessionFactory;
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
import org.springframework.batch.item.database.HibernateCursorItemReader;
import org.springframework.batch.item.database.HibernatePagingItemReader;
import org.springframework.batch.item.database.builder.HibernateCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.HibernatePagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ch.qos.logback.classic.Level;
import io.spring.batch.util.LogLevelUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBatchProcessing
@SpringBootApplication
@RequiredArgsConstructor
public class HibernateMain {

    private static final int CHUNK_SIZE = 3;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public static void main(String[] args) {
        args = new String[] { "city=Dover" };
        SpringApplication.run(HibernateMain.class, args);
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
                                .listener(new JobExecutionListener() {
                                    @Override
                                    public void beforeJob(JobExecution jobExecution) {
                                        // LogLevelUtil.setLevel("p6spy", Level.INFO);
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
                                 //.reader(cursorItemReader(null, null))
                                 .reader(pagingItemReader(null, null))
                                 .writer(customerItemWriter())
                                 // .listener(new LoggingChunkListener())
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
    public HibernateCursorItemReader<Customer> cursorItemReader(EntityManagerFactory entityManagerFactory,
                                                                @Value("#{jobParameters['city']}") String city) {

        return new HibernateCursorItemReaderBuilder<Customer>()
                .name("customerItemReader")
                .sessionFactory(entityManagerFactory.unwrap(SessionFactory.class))
                .queryString("from Customer where city = :city")
                // .queryName(String) // 하이버네이트 구성에 포함된 네임드 하이버네이트 쿼리를 참조함
                // .queryProvider(HibernateQueryProvider<T>) // 하이버네이트 쿼리(HQL) 프로그래밍으로 빌드하는 기능 제공
                // .nativeQuery(String) // 네이티브 SQL 쿼리를 실행한 뒤 결과를 하이버네이트로 매핑하는데 사용
                .parameterValues(Collections.singletonMap("city", city))
                .build();
        /*
            select
                customer0_.id as id1_0_,
                customer0_.address as address2_0_,
                customer0_.city as city3_0_,
                customer0_.firstName as firstnam4_0_,
                customer0_.lastName as lastname5_0_,
                customer0_.middleInitial as middlein6_0_,
                customer0_.state as state7_0_,
                customer0_.zipCode as zipcode8_0_
            from
                customer customer0_
            where
                customer0_.city=${city}
         */
    }

    @Bean
    @StepScope
    public HibernatePagingItemReader<Customer> pagingItemReader(EntityManagerFactory entityManagerFactory,
                                                                @Value("#{jobParameters['city']}") String city) {

        return new HibernatePagingItemReaderBuilder<Customer>()
                .name("customerItemReader")
                .sessionFactory(entityManagerFactory.unwrap(SessionFactory.class))
                .queryString("from Customer where city = :city")
                // .queryName(String)
                // .queryProvider()
                .parameterValues(Collections.singletonMap("city", city))
                .pageSize(CHUNK_SIZE)
                .build();
        /*
            select
                customer0_.id as id1_0_,
                customer0_.address as address2_0_,
                customer0_.city as city3_0_,
                customer0_.firstName as firstnam4_0_,
                customer0_.lastName as lastname5_0_,
                customer0_.middleInitial as middlein6_0_,
                customer0_.state as state7_0_,
                customer0_.zipCode as zipcode8_0_
            from
                customer customer0_
            where
                customer0_.city= ${city}
            limit ${offset}, ${pageSize};
         */
    }
}

