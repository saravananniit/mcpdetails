package com.bank.repository;

import com.bank.model.Account;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Repository for Account entity.
 * Uses in-memory storage with thread-safe operations.
 */
public class AccountRepository {
    private final Map<String, Account> accounts;

    public AccountRepository() {
        this.accounts = new ConcurrentHashMap<>();
    }

    public Account save(Account account) {
        accounts.put(account.getAccountId(), account);
        return account;
    }

    public Optional<Account> findById(String accountId) {
        return Optional.ofNullable(accounts.get(accountId));
    }

    public List<Account> findByCustomerId(String customerId) {
        return accounts.values().stream()
                .filter(account -> account.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    public List<Account> findAll() {
        return new ArrayList<>(accounts.values());
    }

    public List<Account> findAllActive() {
        return accounts.values().stream()
                .filter(Account::isActive)
                .collect(Collectors.toList());
    }

    public void deleteById(String accountId) {
        accounts.remove(accountId);
    }

    public boolean existsById(String accountId) {
        return accounts.containsKey(accountId);
    }

    public long count() {
        return accounts.size();
    }

    public void clear() {
        accounts.clear();
    }
}