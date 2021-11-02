package io.spring.batch.example18;

import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;

import io.spring.batch.domain.Customer;

public class CustomerClassifier implements Classifier<Customer, ItemWriter<? super Customer>> {

    private ItemWriter<Customer> fileItemWriter;
    private ItemWriter<Customer> jdbcItemWriter;

    @Override
    public ItemWriter<? super Customer> classify(Customer customer) {
        if (customer.getState().matches("^[A-M].*")) {
            return fileItemWriter;
        }
        return jdbcItemWriter;
    }
}
