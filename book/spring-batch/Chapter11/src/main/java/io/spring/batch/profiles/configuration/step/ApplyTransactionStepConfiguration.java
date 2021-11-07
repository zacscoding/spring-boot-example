package io.spring.batch.profiles.configuration.step;

import javax.sql.DataSource;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.spring.batch.profiles.configuration.properties.BatchProperties;
import io.spring.batch.profiles.domain.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApplyTransactionStepConfiguration {

    private final StepBuilderFactory stepBuilderFactory;
    private final BatchProperties batchProperties;

    @Bean
    Step applyTransactions() {
        return stepBuilderFactory.get("applyTransactions")
                                 .<Transaction, Transaction>chunk(batchProperties.getApplyTransactionChunkSize())
                                 .reader(applyTransactionReader(null))
                                 .writer(applyTransactionWriter(null))
                                 .build();
    }

    @Bean
    JdbcCursorItemReader<Transaction> applyTransactionReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Transaction>()
                .name("applyTransactionReader")
                .dataSource(dataSource)
                .sql("select transaction_id, " +
                     "account_account_id, " +
                     "description, " +
                     "credit, " +
                     "debit, " +
                     "timestamp " +
                     "from transaction " +
                     "order by timestamp")
                .rowMapper((resultSet, i) -> Transaction.builder()
                                                        .transactionId(resultSet.getLong("transaction_id"))
                                                        .accountId(resultSet.getLong("account_account_id"))
                                                        .description(resultSet.getString("description"))
                                                        .credit(resultSet.getBigDecimal("credit"))
                                                        .debit(resultSet.getBigDecimal("debit"))
                                                        .timestamp(resultSet.getTimestamp("timestamp"))
                                                        .build())
                .build();
    }

    @Bean
    JdbcBatchItemWriter<Transaction> applyTransactionWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Transaction>()
                .dataSource(dataSource)
                .sql("UPDATE account SET " +
                     "balance = balance + :transactionAmount " +
                     "WHERE account_id = :accountId")
                .beanMapped()
                .build();
    }
}
