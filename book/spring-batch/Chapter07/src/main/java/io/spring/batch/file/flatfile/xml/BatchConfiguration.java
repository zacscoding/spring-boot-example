package io.spring.batch.file.flatfile.xml;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import io.spring.batch.file.domain.Customer;
import io.spring.batch.file.domain.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile({ "flat-file-xml" })
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
    public StaxEventItemReader<Customer> customerFileReader(@Value("#{jobParameters['customerFile']}")
                                                                    Resource inputFile) {

        return new StaxEventItemReaderBuilder<Customer>()
                .name("customerFileReader")
                .resource(inputFile)
                .addFragmentRootElements("customer")
                .unmarshaller(customerMarshaller())
                .build();
    }

    private Jaxb2Marshaller customerMarshaller() {
        final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

        marshaller.setClassesToBeBound(Customer.class, Transaction.class);

        return marshaller;
    }
}