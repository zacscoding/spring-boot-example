package io.spring.batch.example17;

import java.util.Arrays;

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
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

import io.spring.batch.domain.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBatchProcessing
@SpringBootApplication
@RequiredArgsConstructor
public class MultiResourceItemWriterMain {

    public static void main(String[] args) {
        args = new String[] {
                "customerFile=input/customer.csv"
        };
        SpringApplication.run(MultiResourceItemWriterMain.class, args);
    }

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    @StepScope
    public FlatFileItemReader<Customer> customerItemReader(
            @Value("#{jobParameters['customerFile']}") Resource inputFile) {

        return new FlatFileItemReaderBuilder<Customer>()
                .name("customerItemReader")
                .delimited()
                .names("firstName",
                       "middleInitial",
                       "lastName",
                       "address",
                       "city",
                       "state",
                       "zip")
                .targetType(Customer.class)
                .resource(inputFile)
                .build();
    }

    @Bean
    public ItemWriter<Customer> itemWriter1() {
        return customers -> {
            System.out.printf("## Writer-1 items: %d\n", customers.size());
            for (Customer customer : customers) {
                System.out.println("> " + customer);
            }
        };
    }

    @Bean
    public ItemWriter<Customer> itemWriter2() {
        return customers -> {
            System.out.printf("## Writer-2 items: %d\n", customers.size());
            for (Customer customer : customers) {
                System.out.println("> " + customer);
            }
        };
    }

    @Bean
    public CompositeItemWriter<Customer> compositeItemWriter() {
        return new CompositeItemWriterBuilder<Customer>()
                .delegates(
                        Arrays.asList(itemWriter1(), itemWriter2())
                ).build();
    }

    @Bean
    public Step copyFileStep() {
        return stepBuilderFactory.get("copyFileStep")
                                 .<Customer, Customer>chunk(3)
                                 .reader(customerItemReader(null))
                                 .writer(compositeItemWriter())
                                 .build();
    }

    @Bean
    public Job job() throws Exception {
        return jobBuilderFactory.get("job")
                                .incrementer(new RunIdIncrementer())
                                .start(copyFileStep())
                                .build();
    }
}

