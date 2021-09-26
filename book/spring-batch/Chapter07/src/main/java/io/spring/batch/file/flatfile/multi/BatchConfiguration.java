package io.spring.batch.file.flatfile.multi;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.validation.BindException;

import io.spring.batch.file.domain.Customer;
import io.spring.batch.file.domain.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile({ "flat-file-multi-object" })
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
                                 .reader(customerFileReader())
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
    public FlatFileItemReader<Object> customerItemReader(@Value("#{jobParameters['customerFile']}")
                                                                 Resource inputFile) {
        logger.info("Activated flat file reader: multi records");

        return new FlatFileItemReaderBuilder<>()
                .name("customerItemReader")
                .lineMapper(lineTokenizer())
                .resource(inputFile)
                .build();
    }

    @Bean
    public CustomerFileReader customerFileReader() {
        return new CustomerFileReader(customerItemReader(null));
    }

    @Bean
    public PatternMatchingCompositeLineMapper lineTokenizer() {
        final Map<String, LineTokenizer> lineTokenizers = new HashMap<>(2);

        lineTokenizers.put("CUST*", customerLineTokenizer());
        lineTokenizers.put("TRANS*", transactionLineTokenizer());

        final Map<String, FieldSetMapper> fieldSetMappers = new HashMap<>(2);

        final BeanWrapperFieldSetMapper<Customer> customerFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        customerFieldSetMapper.setTargetType(Customer.class);

        fieldSetMappers.put("CUST*", customerFieldSetMapper);
        fieldSetMappers.put("TRANS*", new TransactionFieldSetMapper());

        final PatternMatchingCompositeLineMapper lineMappers = new PatternMatchingCompositeLineMapper();

        lineMappers.setTokenizers(lineTokenizers);
        lineMappers.setFieldSetMappers(fieldSetMappers);

        return lineMappers;
    }

    @Bean
    public DelimitedLineTokenizer transactionLineTokenizer() {
        final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        lineTokenizer.setNames("prefix", "accountNumber", "transactionDate", "amount");

        return lineTokenizer;
    }

    @Bean
    public DelimitedLineTokenizer customerLineTokenizer() {
        final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        lineTokenizer.setNames("firstName",
                               "middleInitial",
                               "lastName",
                               "address",
                               "city",
                               "state",
                               "zipCode");
        lineTokenizer.setIncludedFields(1, 2, 3, 4, 5, 6, 7);

        return lineTokenizer;
    }

    public static class TransactionFieldSetMapper implements FieldSetMapper<Transaction> {

        @Override
        public Transaction mapFieldSet(FieldSet fieldSet) throws BindException {
            return Transaction.builder()
                              .accountNumber(fieldSet.readString("accountNumber"))
                              .amount(fieldSet.readDouble("amount"))
                              .transactionDate(fieldSet.readDate("transactionDate", "yyyy-MM-dd HH:mm:ss"))
                              .build();
        }
    }
}
