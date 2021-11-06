package io.spring.batch.configuration.step;

import javax.sql.DataSource;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemWriter;
import org.springframework.batch.item.file.builder.MultiResourceItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import io.spring.batch.batch.AccountItemProcessor;
import io.spring.batch.batch.StatementHeaderCallback;
import io.spring.batch.batch.StatementLineAggregator;
import io.spring.batch.domain.Customer;
import io.spring.batch.domain.Statement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GenerateStatementStepConfiguration {

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    Step generateStatements(AccountItemProcessor itemProcessor) {
        return stepBuilderFactory.get("generateStatements")
                                 .<Statement, Statement>chunk(1)
                                 .reader(statementItemReader(null))
                                 .processor(itemProcessor)
                                 .writer(statementItemWriter(null))
                                 .build();
    }

    @Bean
    JdbcCursorItemReader<Statement> statementItemReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Statement>()
                .name("statementItemReader")
                .dataSource(dataSource)
                .sql("SELECT * FROM customer")
                .rowMapper((resultSet, i) -> {
                    Customer customer = new Customer(resultSet.getLong("customer_id"),
                                                     resultSet.getString("first_name"),
                                                     resultSet.getString("middle_name"),
                                                     resultSet.getString("last_name"),
                                                     resultSet.getString("address1"),
                                                     resultSet.getString("address2"),
                                                     resultSet.getString("city"),
                                                     resultSet.getString("state"),
                                                     resultSet.getString("postal_code"),
                                                     resultSet.getString("ssn"),
                                                     resultSet.getString("email_address"),
                                                     resultSet.getString("home_phone"),
                                                     resultSet.getString("cell_phone"),
                                                     resultSet.getString("work_phone"),
                                                     resultSet.getInt("notification_pref"));

                    return new Statement(customer);
                }).build();
    }

    @Bean
    @StepScope
    MultiResourceItemWriter<Statement> statementItemWriter(@Value("#{jobParameters['outputDirectory']}")
                                                                   Resource outputDir) {
        return new MultiResourceItemWriterBuilder<Statement>()
                .name("statementItemWriter")
                .resource(outputDir)
                .itemCountLimitPerResource(1)
                .delegate(individualStatementItemWriter())
                .build();
    }

    @Bean
    FlatFileItemWriter<Statement> individualStatementItemWriter() {
        final FlatFileItemWriter<Statement> itemWriter = new FlatFileItemWriter<>();

        itemWriter.setName("individualStatementItemWriter");
        itemWriter.setHeaderCallback(new StatementHeaderCallback());
        itemWriter.setLineAggregator(new StatementLineAggregator());

        return itemWriter;
    }
}
