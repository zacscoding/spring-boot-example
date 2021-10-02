package io.spring.batch.database.example8;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import io.spring.batch.database.domain.Customer;
import io.spring.batch.database.domain.CustomerGenerator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomerService {

    private List<Customer> customers;
    private int curIndex;

    @PostConstruct
    private void setUp() {
        curIndex = 0;
        customers = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            customers.add(CustomerGenerator.createCustomer());
        }
    }

    public Customer getCustomer() {
        if (curIndex >= customers.size()) {
            return null;
        }
        return customers.get(curIndex++);
    }
}
