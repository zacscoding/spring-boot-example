package io.spring.batch.profiles.configuration.step;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.file.transform.PatternMatchingCompositeLineTokenizer;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import com.google.common.collect.ImmutableMap;

import io.spring.batch.profiles.batch.CustomerItemValidator;
import io.spring.batch.profiles.batch.CustomerUpdateClassifier;
import io.spring.batch.profiles.configuration.properties.BatchProperties;
import io.spring.batch.profiles.domain.CustomerAddressUpdate;
import io.spring.batch.profiles.domain.CustomerContractUpdate;
import io.spring.batch.profiles.domain.CustomerNameUpdate;
import io.spring.batch.profiles.domain.CustomerUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CustomerUpdateStepConfiguration {

    private final StepBuilderFactory stepBuilderFactory;
    private final BatchProperties batchProperties;

    @Bean
    Step importCustomerUpdates() throws Exception {
        return stepBuilderFactory.get("importCustomerUpdates")
                                 .<CustomerUpdate, CustomerUpdate>chunk(batchProperties.getCustomerUpdateChunkSize())
                                 .reader(customerUpdateItemReader(null))
                                 .processor(customerValidatingItemProcessor(null))
                                 .writer(customerUpdateItemWriter())
                                 .build();
    }

    @Bean
    @StepScope
    FlatFileItemReader<CustomerUpdate> customerUpdateItemReader(
            @Value("#{jobParameters['customerUpdateFile']}") Resource inputFile) throws Exception {
        return new FlatFileItemReaderBuilder<CustomerUpdate>()
                .name("customerUpdateItemReader")
                .resource(inputFile)
                .lineTokenizer(customerUpdatesLineTokenizer())
                .fieldSetMapper(customerUpdateFieldSetMapper())
                .build();
    }

    /**
     * 3가지의 레코드 타입이 존재하며 해당 필드가 비어있는 경우 갱신하지 않는다.
     */
    @Bean
    LineTokenizer customerUpdatesLineTokenizer() throws Exception {
        // Record type - 1
        DelimitedLineTokenizer recordType1 = new DelimitedLineTokenizer();

        recordType1.setNames("recordId", "customerId", "firstName", "middleName", "lastName");
        recordType1.afterPropertiesSet();

        // Record type - 2
        DelimitedLineTokenizer recordType2 = new DelimitedLineTokenizer();

        recordType2.setNames("recordId", "customerId", "address1", "address2", "city", "state", "postalCode");
        recordType2.afterPropertiesSet();

        // Record type - 3
        DelimitedLineTokenizer recordType3 = new DelimitedLineTokenizer();

        recordType3.setNames("recordId", "customerId", "emailAddress", "homePhone", "cellPhone", "workPhone",
                             "notificationPreference");
        recordType3.afterPropertiesSet();

        Map<String, LineTokenizer> tokenizers = ImmutableMap.of(
                "1*", recordType1,
                "2*", recordType2,
                "3*", recordType3
        );

        PatternMatchingCompositeLineTokenizer lineTokenizer = new PatternMatchingCompositeLineTokenizer();
        lineTokenizer.setTokenizers(tokenizers);

        return lineTokenizer;
    }

    @Bean
    FieldSetMapper<CustomerUpdate> customerUpdateFieldSetMapper() {
        return fieldSet -> {
            switch (fieldSet.readInt("recordId")) {
                case 1:
                    return new CustomerNameUpdate(
                            fieldSet.readLong("customerId"),
                            checkHasText(fieldSet.readString("firstName")),
                            checkHasText(fieldSet.readString("middleName")),
                            checkHasText(fieldSet.readString("lastName"))
                    );
                case 2:
                    return new CustomerAddressUpdate(
                            fieldSet.readLong("customerId"),
                            checkHasText(fieldSet.readString("address1")),
                            checkHasText(fieldSet.readString("address2")),
                            checkHasText(fieldSet.readString("city")),
                            checkHasText(fieldSet.readString("state")),
                            checkHasText(fieldSet.readString("postalCode"))
                    );
                case 3:
                    final String rawPreference = fieldSet.readString("notificationPreference");
                    return new CustomerContractUpdate(
                            fieldSet.readLong("customerId"),
                            checkHasText(fieldSet.readString("emailAddress")),
                            checkHasText(fieldSet.readString("homePhone")),
                            checkHasText(fieldSet.readString("cellPhone")),
                            checkHasText(fieldSet.readString("workPhone")),
                            StringUtils.hasText(rawPreference) ? Integer.parseInt(
                                    rawPreference) : null
                    );
                default:
                    throw new IllegalArgumentException(
                            "Invalid record type was found: " + fieldSet.readInt("recordId"));
            }
        };
    }

    @Bean
    ValidatingItemProcessor<CustomerUpdate> customerValidatingItemProcessor(CustomerItemValidator validator) {
        ValidatingItemProcessor<CustomerUpdate> processor = new ValidatingItemProcessor<>(validator);

        processor.setFilter(true);

        return processor;
    }

    @Bean
    JdbcBatchItemWriter<CustomerUpdate> customerNameUpdateItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<CustomerUpdate>()
                .beanMapped()
                .sql("UPDATE customer " +
                     "SET first_name = COALESCE(:firstName, first_name), " +
                     "middle_name = COALESCE(:middleName, middle_name), " +
                     "last_name = COALESCE(:lastName, last_name) " +
                     "WHERE customer_id = :customerId")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    JdbcBatchItemWriter<CustomerUpdate> customerAddressUpdateItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<CustomerUpdate>()
                .beanMapped()
                .sql("UPDATE customer SET " +
                     "address1 = COALESCE(:address1, address1), " +
                     "address2 = COALESCE(:address2, address2), " +
                     "city = COALESCE(:city, city), " +
                     "state = COALESCE(:state, state), " +
                     "postal_code = COALESCE(:postalCode, postal_code) " +
                     "WHERE customer_id = :customerId")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    JdbcBatchItemWriter<CustomerUpdate> customerContactUpdateItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<CustomerUpdate>()
                .beanMapped()
                .sql("UPDATE customer SET " +
                     "email_address = COALESCE(:emailAddress, email_address), " +
                     "home_phone = COALESCE(:homePhone, home_phone), " +
                     "cell_phone = COALESCE(:cellPhone, cell_phone), " +
                     "work_phone = COALESCE(:workPhone, work_phone), " +
                     "notification_pref = COALESCE(:notificationPreferences, notification_pref) " +
                     "WHERE customer_id = :customerId")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    ClassifierCompositeItemWriter<CustomerUpdate> customerUpdateItemWriter() {
        final CustomerUpdateClassifier classifier = new CustomerUpdateClassifier(
                customerNameUpdateItemWriter(null),
                customerAddressUpdateItemWriter(null),
                customerContactUpdateItemWriter(null)
        );

        final ClassifierCompositeItemWriter<CustomerUpdate> compositeItemWriter = new ClassifierCompositeItemWriter<>();
        compositeItemWriter.setClassifier(classifier);

        return compositeItemWriter;
    }

    private String checkHasText(String value) {
        return StringUtils.hasText(value) ? value : null;
    }
}
