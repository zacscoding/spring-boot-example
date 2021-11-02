package io.spring.batch.example13;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
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
public class ItemWriterAdapterMain {

    public static void main(String[] args) {
        args = new String[] {
                "customerFile=input/customer.csv"
        };
        SpringApplication.run(ItemWriterAdapterMain.class, args);
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
    public ItemWriterAdapter<Customer> itemWriter(CustomerService service) {
        ItemWriterAdapter<Customer> adapter = new ItemWriterAdapter<>();

        adapter.setTargetObject(service);
        adapter.setTargetMethod("logCustomer");

        return adapter;
    }

    @Bean
    public Step copyFileStep() {
        return stepBuilderFactory.get("copyFileStep")
                                 .<Customer, Customer>chunk(3)
                                 .reader(customerItemReader(null))
                                 .writer(itemWriter(null))
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
