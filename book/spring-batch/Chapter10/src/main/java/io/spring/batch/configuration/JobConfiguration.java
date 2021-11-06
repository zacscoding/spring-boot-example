package io.spring.batch.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class JobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    public JobConfiguration(JobBuilderFactory jobBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
    }

    @Bean
    Job job(@Qualifier("importCustomerUpdates") Step importCustomerUpdates,
            @Qualifier("importTransactions") Step importTransactions,
            @Qualifier("generateStatements") Step generateStatements) {
        return jobBuilderFactory.get("importJob")
                                .incrementer(new RunIdIncrementer())
                                .start(importCustomerUpdates)
                                .next(importTransactions)
                                .next(generateStatements)
                                .build();
    }

}
