package io.spring.batch.configuration.step;

import javax.sql.DataSource;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import io.spring.batch.configuration.properties.BatchProperties;
import io.spring.batch.domain.Transaction;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ImportTransactionStepConfiguration {

    private final StepBuilderFactory stepBuilderFactory;
    private final BatchProperties batchProperties;

    @Bean
    Step importTransactions() throws Exception {
        return stepBuilderFactory.get("importTransaction")
                                 .<Transaction, Transaction>chunk(batchProperties.getImportTransactionChunkSize())
                                 .reader(transactionItemReader(null))
                                 .writer(transactionItemWriter(null))
                                 .build();
    }

    @Bean
    @StepScope
    StaxEventItemReader<Transaction> transactionItemReader(
            @Value("#{jobParameters['transactionFile']}") Resource transactionFile) throws Exception {

        final Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
        unmarshaller.setClassesToBeBound(Transaction.class);

        return new StaxEventItemReaderBuilder<Transaction>()
                .name("fooReader")
                .resource(transactionFile)
                .addFragmentRootElements("transaction")
                .unmarshaller(unmarshaller)
                .build();
    }

    @Bean
    JdbcBatchItemWriter<Transaction> transactionItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Transaction>()
                .dataSource(dataSource)
                .sql("INSERT INTO transaction (transaction_id, " +
                     "account_account_id, " +
                     "description, " +
                     "credit, " +
                     "debit, " +
                     "timestamp) VALUES (:transactionId, " +
                     ":accountId, " +
                     ":description, " +
                     ":credit, " +
                     ":debit, " +
                     ":timestamp)")
                .beanMapped()
                .build();
    }
}
