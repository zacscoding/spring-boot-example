package io.spring.batch.example14;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerService {

    public void logCustomerAddress(String address, String city, String state, String zip) {
        System.out.printf("I just saved the address. address: %s, city: %s, state: %s, zip: %s\n",
                          address, city, state, zip);
    }
}
