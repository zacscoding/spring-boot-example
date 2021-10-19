package io.spring.batch.example5;

import org.springframework.stereotype.Service;

import io.spring.batch.domain.Customer;

@Service
public class UpperCaseNameService {

    public Customer upperCase(Customer customer) {
        return Customer.builder()
                       .firstName(customer.getFirstName().toUpperCase())
                       .middleInitial(customer.getMiddleInitial().toUpperCase())
                       .lastName(customer.getLastName().toUpperCase())
                       .address(customer.getAddress())
                       .city(customer.getCity())
                       .state(customer.getState())
                       .zip(customer.getZip())
                       .build();
    }
}
