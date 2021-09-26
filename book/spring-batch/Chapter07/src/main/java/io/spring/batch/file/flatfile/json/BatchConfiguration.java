package io.spring.batch.file.flatfile.json;

import java.text.SimpleDateFormat;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.spring.batch.file.domain.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile({ "flat-file-json" })
@Configuration
@RequiredArgsConstructor
public class BatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

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
                                 .<Customer, Customer>chunk(5)
                                 .reader(customerFileReader(null))
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

    @Bean
    @StepScope
    public JsonItemReader<Customer> customerFileReader(@Value("#{jobParameters['customerFile']}")
                                                               Resource inputFile) {
        // dateformat 커스터 마이징 때문에 Bean 대신 별도로 생성한다.
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));

        final JacksonJsonObjectReader<Customer> jsonObjectReader = new JacksonJsonObjectReader<>(objectMapper,
                                                                                                 Customer.class);

        return new JsonItemReaderBuilder<Customer>()
                .name("customerFileReader")
                .jsonObjectReader(jsonObjectReader)
                .resource(inputFile)
                .build();
    }
}