package io.spring.batch.file.flatfile.single;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.spring.batch.file.domain.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile({ "flat-file-single" })
@Configuration
@RequiredArgsConstructor
public class BatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final FlatFileItemReader<Customer> reader;

    @Bean
    public Job copyCustomerJob() {
        return jobBuilderFactory.get("copyCustomerJob")
                                .incrementer(new RunIdIncrementer())
                                .start(copyFileStep())
                                .build();
    }

    @Bean
    public Step copyFileStep() {
        return stepBuilderFactory.get("copyFileStep")
                                 .<Customer, Customer>chunk(10)
                                 .reader(reader)
                                 .writer(customerConsoleItemWriter())
                                 .build();
    }

    @Bean
    public ItemWriter<Customer> customerConsoleItemWriter() {
        return items -> {
            logger.info("Write items: {}", items.size());
            items.forEach(item -> logger.info(item.toString()));
        };
    }
}
