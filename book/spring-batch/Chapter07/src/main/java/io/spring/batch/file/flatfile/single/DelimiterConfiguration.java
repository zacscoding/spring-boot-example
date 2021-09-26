package io.spring.batch.file.flatfile.single;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

import io.spring.batch.file.domain.Customer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile({ "flat-file-single-delimiter" })
@Configuration
public class DelimiterConfiguration {

    @Bean
    @StepScope
    public FlatFileItemReader<Customer> customerFlatFileItemReader(
            @Value("#{jobParameters['customerFile']}") Resource inputFile) {

        logger.info("Activated flat file reader: delimited");

        return new FlatFileItemReaderBuilder<Customer>()
                // ItemStream 인터페이스는 애플리케이션 내 각 스텝의 ExecutionContext에 추가되는 특정 키의
                // 접두문자로 사용될 이름이 필요
                .name("customerItemReader")
                .resource(inputFile)
                .delimited()
                .delimiter(",") // default ","
                .names("firstName",
                       "middleInitial",
                       "lastName",
                       "addressNumber",
                       "street",
                       "city",
                       "state",
                       "zipCode")
                .targetType(Customer.class)
                .build();
    }
}
