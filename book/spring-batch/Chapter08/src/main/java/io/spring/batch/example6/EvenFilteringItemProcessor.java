package io.spring.batch.example6;

import org.springframework.batch.item.ItemProcessor;

import io.spring.batch.domain.Customer;

public class EvenFilteringItemProcessor implements ItemProcessor<Customer, Customer> {

    @Override
    public Customer process(Customer item) throws Exception {
        return Integer.parseInt(item.getZip()) % 2 == 0 ? null : item;
    }
}
