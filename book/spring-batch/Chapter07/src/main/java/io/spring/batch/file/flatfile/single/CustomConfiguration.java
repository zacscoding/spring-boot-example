package io.spring.batch.file.flatfile.single;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.validation.BindException;

import io.spring.batch.file.domain.Customer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile({ "flat-file-single-custom-field-mapper" })
@Configuration
public class CustomConfiguration {

    /**
     * {@link BeanWrapperFieldSetMapper} 대신 {@link CustomerFieldMapper}를 이용한다.
     */
    @Bean
    @StepScope
    public FlatFileItemReader<Customer> customerFlatFileItemReader(
            @Value("#{jobParameters['customerFile']}") Resource inputFile) {
        logger.info("Activated flat file reader: custom field mapper");

        return new FlatFileItemReaderBuilder<Customer>()
                // ItemStream 인터페이스는 애플리케이션 내 각 스텝의 ExecutionContext에 추가되는 특정 키의
                // 접두문자로 사용될 이름이 필요
                .name("customerItemReader")
                .delimited()
                .names("firstName",
                       "middleInitial",
                       "lastName",
                       "addressNumber",
                       "street",
                       "city",
                       "state",
                       "zipCode")
                .fieldSetMapper(new CustomerFieldMapper())
                .resource(inputFile)
                .build();
    }

    public static class CustomerFieldMapper implements FieldSetMapper<Customer> {

        @Override
        public Customer mapFieldSet(FieldSet fieldSet) throws BindException {
            return Customer.builder()
                           .firstName(fieldSet.readString("firstName"))
                           .middleInitial(fieldSet.readString("middleInitial"))
                           .lastName(fieldSet.readString("lastName"))
                           .address(fieldSet.readString("addressNumber") + " " + fieldSet.readString("street"))
                           .city(fieldSet.readString("city"))
                           .state(fieldSet.readString("state"))
                           .zipCode(fieldSet.readString("zipCode"))
                           .build();
        }
    }
}
