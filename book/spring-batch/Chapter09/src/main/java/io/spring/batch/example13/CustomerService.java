package io.spring.batch.example13;

import org.springframework.stereotype.Service;

import io.spring.batch.domain.Customer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerService {

    public void logCustomer(Customer customer) {
        //logger.info("I just saved: {}", customer);
        System.out.println("I just saved: " + customer);
    }
}
