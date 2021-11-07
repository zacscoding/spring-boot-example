package io.spring.batch.profiles;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import io.spring.batch.profiles.configuration.properties.BatchProperties;

@EnableBatchProcessing
@SpringBootApplication
@EnableConfigurationProperties(BatchProperties.class)
public class ApplicationMain {

    public static void main(String[] args) {
        args = new String[] {
                "customerUpdateFile=input/customer_update.csv",
                "transactionFile=input/transactions.xml",
                "outputDirectory=output/"
        };
        SpringApplication.run(ApplicationMain.class, args);
    }
}
