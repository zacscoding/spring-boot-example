package demo.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import demo.batch.transaction.JobCompletionNotificationListener;
import demo.batch.transaction.TransactionProcessor;
import demo.batch.transaction.TransactionWriter;
import demo.transaction.domain.Transaction;
import demo.transaction.domain.TransactionState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchTransactionConfiguration {

    private final int chunkSize = 4;

    // required
    @PersistenceUnit
    private final EntityManagerFactory factory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final TransactionProcessor transactionProcessor;
    private final TransactionWriter transactionWriter;

    @Bean
    @StepScope
    public JpaPagingItemReader<Transaction> transactionReader(
            @Value("#{jobParameters['clusterId']}") Long id) {

        logger.info("## Check cluster id : {}", id);

        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("state", TransactionState.MINED);

        return new JpaPagingItemReaderBuilder<Transaction>()
                .pageSize(chunkSize)
                .name("transactionReader")
                .entityManagerFactory(factory)
                .queryString("select t from Transaction t where t.state <> :state")
                .parameterValues(parameterValues)
                .build();
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("txProcessJob")
                                .incrementer(new RunIdIncrementer())
                                .listener(listener)
                                .start(step1)
                                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("tx")
                .<Transaction, Transaction>chunk(chunkSize)
                .reader(transactionReader(null))
                .processor(transactionProcessor)
                .writer(transactionWriter)
                .build();
    }
}
