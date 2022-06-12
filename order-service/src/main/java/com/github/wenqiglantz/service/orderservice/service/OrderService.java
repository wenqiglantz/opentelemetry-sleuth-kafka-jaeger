package com.github.wenqiglantz.service.orderservice.service;

import com.github.wenqiglantz.service.orderservice.data.event.CustomerWasCreated;
import com.github.wenqiglantz.service.orderservice.data.event.CustomerWasDeleted;
import com.github.wenqiglantz.service.orderservice.data.event.CustomerWasUpdated;
import com.github.wenqiglantz.service.orderservice.persistence.entity.Customer;
import com.github.wenqiglantz.service.orderservice.persistence.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderService {

    private final CustomerRepository customerRepository;

    public void consumeCustomerWasCreated(CustomerWasCreated customerWasCreated) {
        String customerId = customerWasCreated.getCustomerId();
        Optional<Customer> customerOptional = customerRepository.findByCustomerId(customerId);
        if (!(customerOptional).isPresent()) {
            log.debug("customer not existing: ", customerId);
            Customer customer = Customer.builder()
                    .customerId(customerWasCreated.getCustomerId())
                    .firstName(customerWasCreated.getFirstName())
                    .lastName(customerWasCreated.getLastName())
                    .build();
            customerRepository.save(customer);
        }
    }

    public void consumeCustomerWasUpdated(CustomerWasUpdated customerWasUpdated) {
        String customerId = customerWasUpdated.getCustomerId();
        Optional<Customer> customerOptional = customerRepository.findByCustomerId(customerId);
        if (customerOptional.isPresent()) {
            log.info("Customer updated: ", customerId);
            customerOptional.get().setFirstName(customerWasUpdated.getFirstName());
            customerOptional.get().setLastName(customerWasUpdated.getLastName());
            customerRepository.save(customerOptional.get());
        }
    }

    public void consumeCustomerWasDeleted(CustomerWasDeleted customerWasDeleted) {
        String customerId = customerWasDeleted.getCustomerId();
        Optional<Customer> customerOptional = customerRepository.findByCustomerId(customerId);
        if (customerOptional.isPresent()) {
            log.debug("customer deleted: ", customerId);
            customerRepository.delete(customerOptional.get());
        }
    }
}
