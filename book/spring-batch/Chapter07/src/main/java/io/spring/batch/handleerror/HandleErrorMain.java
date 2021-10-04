package io.spring.batch.handleerror;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import ch.qos.logback.classic.Level;
import io.spring.batch.file.domain.Customer;
import io.spring.batch.util.LogLevelUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBatchProcessing
@SpringBootApplication
@RequiredArgsConstructor
public class HandleErrorMain {

    private static final int CHUNK_SIZE = 3;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public static void main(String[] args) {
        args = new String[] { "customerFile=input/customerFixedWidth_Invalid.txt" };
        SpringApplication.run(HandleErrorMain.class, args);
    }

    @PostConstruct
    private void setUp() {
        LogLevelUtil.setLevel("org.springframework.batch", Level.INFO);
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
                                 // .reader(customerItemReader(null, null))
                                 // .faultTolerant()
                                 // .skip(Exception.class)
                                 // .noSkip(ParseException.class)
                                 // .skipLimit(10)
                                 .reader(customerFlatFileItemReader(null))
                                 .listener(customerItemListener())
                                 .writer(customerItemWriter())
                                 .faultTolerant()
                                 .skipPolicy(new FileVerificationSkipper())
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
    public FlatFileItemReader<Customer> customerFlatFileItemReader(
            @Value("#{jobParameters['customerFile']}") Resource inputFile) {

        return new FlatFileItemReaderBuilder<io.spring.batch.file.domain.Customer>()
                .name("customerItemReader")
                .resource(inputFile)
                .fixedLength()
                .columns(createRange(new int[][] {
                        { 1, 11 },
                        { 12, 12 },
                        { 13, 22 },
                        { 23, 26 },
                        { 27, 46 },
                        { 47, 62 },
                        { 63, 64 },
                        { 65, 69 }
                }))
                .names("firstName",
                       "middleInitial",
                       "lastName",
                       "addressNumber",
                       "street",
                       "city",
                       "state",
                       "zipCode")
                .targetType(io.spring.batch.file.domain.Customer.class)
                .build();
    }

    @Bean
    public CustomerItemListener customerItemListener() {
        return new CustomerItemListener();
    }

    private Range[] createRange(int[][] minMaxArrays) {
        Range[] ranges = new Range[minMaxArrays.length];
        for (int i = 0; i < minMaxArrays.length; i++) {
            ranges[i] = new Range(minMaxArrays[i][0], minMaxArrays[i][1]);
        }
        return ranges;
    }
}

