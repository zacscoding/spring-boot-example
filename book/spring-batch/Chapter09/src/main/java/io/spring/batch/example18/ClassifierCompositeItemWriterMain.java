package io.spring.batch.example18;

import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.support.builder.ClassifierCompositeItemWriterBuilder;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;

import io.spring.batch.domain.Customer;

public class ClassifierCompositeItemWriterMain {

    @Bean
    public ClassifierCompositeItemWriter<Customer> classifierCompositeItemWriter() {
        Classifier<Customer, ItemWriter<? super Customer>> classifier = new CustomerClassifier();

        return new ClassifierCompositeItemWriterBuilder<Customer>()
                .classifier(classifier)
                .build();
    }
}
