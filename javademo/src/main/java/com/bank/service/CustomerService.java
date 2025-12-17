package com.bank.service;

import com.bank.model.Customer;
import com.bank.repository.CustomerRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service layer for customer operations.
 */
public class CustomerService {
    private static final Logger LOGGER = Logger.getLogger(CustomerService.class.getName());
    
    private final CustomerRepository customerRepository;
    private final ValidationService validationService;

    public CustomerService(CustomerRepository customerRepository,
                          ValidationService validationService) {
        this.customerRepository = customerRepository;
        this.validationService = validationService;
    }

    public Customer createCustomer(String firstName, String lastName, String email,
                                   String phoneNumber, LocalDate dateOfBirth, String address) {
        LOGGER.info(String.format("Creating customer: %s %s", firstName, lastName));
        
        if (customerRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Customer with email " + email + " already exists");
        }

        Customer customer = new Customer.Builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .dateOfBirth(dateOfBirth)
                .address(address)
                .build();

        Customer savedCustomer = customerRepository.save(customer);
        LOGGER.info(String.format("Customer created: %s", savedCustomer.getCustomerId()));
        return savedCustomer;
    }

    public Customer getCustomer(String customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));
    }

    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with email: " + email));
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public List<Customer> getAllActiveCustomers() {
        return customerRepository.findAllActive();
    }

    public void deactivateCustomer(String customerId) {
        Customer customer = getCustomer(customerId);
        customer.deactivate();
        customerRepository.save(customer);
        LOGGER.info(String.format("Customer %s deactivated", customerId));
    }

    public void activateCustomer(String customerId) {
        Customer customer = getCustomer(customerId);
        customer.activate();
        customerRepository.save(customer);
        LOGGER.info(String.format("Customer %s activated", customerId));
    }

    public boolean existsById(String customerId) {
        return customerRepository.existsById(customerId);
    }

    public long getTotalCustomerCount() {
        return customerRepository.count();
    }
}