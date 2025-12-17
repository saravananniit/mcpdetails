package com.bank.repository;

import com.bank.model.Customer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Repository for Customer entity.
 */
public class CustomerRepository {
    private final Map<String, Customer> customers;

    public CustomerRepository() {
        this.customers = new ConcurrentHashMap<>();
    }

    public Customer save(Customer customer) {
        customers.put(customer.getCustomerId(), customer);
        return customer;
    }

    public Optional<Customer> findById(String customerId) {
        return Optional.ofNullable(customers.get(customerId));
    }

    public Optional<Customer> findByEmail(String email) {
        return customers.values().stream()
                .filter(customer -> customer.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public List<Customer> findAll() {
        return new ArrayList<>(customers.values());
    }

    public List<Customer> findAllActive() {
        return customers.values().stream()
                .filter(Customer::isActive)
                .collect(Collectors.toList());
    }

    public void deleteById(String customerId) {
        customers.remove(customerId);
    }

    public boolean existsById(String customerId) {
        return customers.containsKey(customerId);
    }

    public boolean existsByEmail(String email) {
        return customers.values().stream()
                .anyMatch(customer -> customer.getEmail().equalsIgnoreCase(email));
    }

    public long count() {
        return customers.size();
    }

    public void clear() {
        customers.clear();
    }
}