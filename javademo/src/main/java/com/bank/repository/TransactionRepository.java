package com.bank.repository;

import com.bank.model.Transaction;
import com.bank.model.TransactionType;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Repository for Transaction entity.
 */
public class TransactionRepository {
    private final Map<String, Transaction> transactions;

    public TransactionRepository() {
        this.transactions = new ConcurrentHashMap<>();
    }

    public Transaction save(Transaction transaction) {
        transactions.put(transaction.getTransactionId(), transaction);
        return transaction;
    }

    public Optional<Transaction> findById(String transactionId) {
        return Optional.ofNullable(transactions.get(transactionId));
    }

    public List<Transaction> findByAccountId(String accountId) {
        return transactions.values().stream()
                .filter(transaction -> transaction.getAccountId().equals(accountId))
                .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    public List<Transaction> findByAccountIdAndDateRange(String accountId, 
                                                         LocalDateTime start, 
                                                         LocalDateTime end) {
        return transactions.values().stream()
                .filter(t -> t.getAccountId().equals(accountId))
                .filter(t -> !t.getTimestamp().isBefore(start) && 
                            !t.getTimestamp().isAfter(end))
                .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    public List<Transaction> findByType(TransactionType type) {
        return transactions.values().stream()
                .filter(transaction -> transaction.getType() == type)
                .collect(Collectors.toList());
    }

    public List<Transaction> findAll() {
        return new ArrayList<>(transactions.values());
    }

    public long count() {
        return transactions.size();
    }

    public void clear() {
        transactions.clear();
    }
}