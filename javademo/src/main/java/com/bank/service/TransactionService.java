package com.bank.service;

import com.bank.model.Transaction;
import com.bank.model.TransactionType;
import com.bank.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service layer for transaction operations.
 */
public class TransactionService {
    private static final Logger LOGGER = Logger.getLogger(TransactionService.class.getName());
    
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction recordTransaction(String accountId, TransactionType type,
                                        BigDecimal amount, BigDecimal balanceAfter,
                                        String description) {
        Transaction transaction = new Transaction.Builder()
                .accountId(accountId)
                .type(type)
                .amount(amount)
                .balanceAfter(balanceAfter)
                .description(description)
                .build();

        Transaction saved = transactionRepository.save(transaction);
        LOGGER.info(String.format("Transaction recorded: %s", saved.getTransactionId()));
        return saved;
    }

    public Transaction getTransaction(String transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found: " + transactionId));
    }

    public List<Transaction> getAccountTransactions(String accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    public List<Transaction> getAccountTransactionsByDateRange(String accountId,
                                                               LocalDateTime start,
                                                               LocalDateTime end) {
        return transactionRepository.findByAccountIdAndDateRange(accountId, start, end);
    }

    public List<Transaction> getTransactionsByType(TransactionType type) {
        return transactionRepository.findByType(type);
    }

    public BigDecimal getTotalDeposits(String accountId) {
        return transactionRepository.findByAccountId(accountId).stream()
                .filter(t -> t.getType() == TransactionType.DEPOSIT)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalWithdrawals(String accountId) {
        return transactionRepository.findByAccountId(accountId).stream()
                .filter(t -> t.getType() == TransactionType.WITHDRAWAL)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public long getTransactionCount(String accountId) {
        return transactionRepository.findByAccountId(accountId).size();
    }
}