package io.spring.batch.file.flatfile.single;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.DefaultFieldSetFactory;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.FieldSetFactory;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

import io.spring.batch.file.domain.Customer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile({ "flat-file-single-custom-line-tokenizer" })
@Configuration
public class LineTokenizerConfiguration {

    @Bean
    @StepScope
    public FlatFileItemReader<Customer> customerFlatFileItemReader(
            @Value("#{jobParameters['customerFile']}") Resource inputFile) {
        logger.info("Activated flat file reader: custom line tokenizer");

        return new FlatFileItemReaderBuilder<Customer>()
                .name("customerItemReader")
                .lineTokenizer(new CustomerFileLineTokenizer())
                .targetType(Customer.class)
                .resource(inputFile)
                .build();
    }

    public static class CustomerFileLineTokenizer implements LineTokenizer {

        private final String delimiter = ",";
        private final String[] names = {
                "firstName",
                "middleInitial",
                "lastName",
                "address",
                "city",
                "state",
                "zipCode"
        };
        private final FieldSetFactory fieldSetFactory = new DefaultFieldSetFactory();

        @Override
        public FieldSet tokenize(String record) {
            final String[] fields = record.split(delimiter);
            final List<String> parsedFields = new ArrayList<>();

            for (int i = 0; i < fields.length; i++) {
                if (i == 4) {
                    parsedFields.set(i - 1, parsedFields.get(i - 1) + " " + fields[i]);
                    continue;
                }
                parsedFields.add(fields[i]);
            }

            return fieldSetFactory.create(parsedFields.toArray(new String[0]), names);
        }
    }
}
