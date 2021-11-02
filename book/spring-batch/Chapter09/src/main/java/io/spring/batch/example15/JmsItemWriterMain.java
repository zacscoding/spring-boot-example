package io.spring.batch.example15;

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
import org.springframework.batch.item.jms.JmsItemReader;
import org.springframework.batch.item.jms.JmsItemWriter;
import org.springframework.batch.item.jms.builder.JmsItemReaderBuilder;
import org.springframework.batch.item.jms.builder.JmsItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.jms.core.JmsTemplate;

import io.spring.batch.domain.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBatchProcessing
@SpringBootApplication
@RequiredArgsConstructor
public class JmsItemWriterMain {

    public static void main(String[] args) {
        args = new String[] {
                "customerFile=input/customer.csv"
        };
        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES", "*");
        SpringApplication.run(JmsItemWriterMain.class, args);
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
    @StepScope
    public ItemWriter<Customer> consoleItemWriter() {
        return items -> {
            System.out.println("## ItemWriter " + items.size());
            for (Customer item : items) {
                System.out.println("> Item: " + item);
            }
        };
    }

    @Bean
    public JmsItemReader<Customer> jmsItemReader(JmsTemplate jmsTemplate) {
        return new JmsItemReaderBuilder<Customer>()
                .jmsTemplate(jmsTemplate)
                .itemType(Customer.class)
                .build();
    }

    @Bean
    public JmsItemWriter<Customer> jmsItemWriter(JmsTemplate jmsTemplate) {
        return new JmsItemWriterBuilder<Customer>()
                .jmsTemplate(jmsTemplate)
                .build();
    }

    @Bean
    public Step formatInputStep() {
        return stepBuilderFactory.get("formatInputStep")
                                 .<Customer, Customer>chunk(3)
                                 .reader(customerItemReader(null))
                                 .writer(jmsItemWriter(null))
                                 .build();
    }

    @Bean
    public Step formatOutputStep() {
        return stepBuilderFactory.get("formatOutputStep")
                                 .<Customer, Customer>chunk(3)
                                 .reader(jmsItemReader(null))
                                 .writer(consoleItemWriter())
                                 .build();
    }

    @Bean
    public Job job() throws Exception {
        return jobBuilderFactory.get("job")
                                .incrementer(new RunIdIncrementer())
                                .start(formatInputStep())
                                .next(formatOutputStep())
                                .build();
    }
}
