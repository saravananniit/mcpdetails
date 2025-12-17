package com.bank.service;

import java.math.BigDecimal;
import java.util.logging.Logger;

/**
 * Service for common validation logic.
 */
public class ValidationService {
    private static final Logger LOGGER = Logger.getLogger(ValidationService.class.getName());
    private static final BigDecimal MIN_TRANSACTION_AMOUNT = new BigDecimal("0.01");
    private static final BigDecimal MAX_TRANSACTION_AMOUNT = new BigDecimal("1000000.00");

    public void validateAmount(BigDecimal amount, String operationType) {
        if (amount == null) {
            throw new IllegalArgumentException(operationType + " amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(operationType + " amount must be positive");
        }
        if (amount.compareTo(MIN_TRANSACTION_AMOUNT) < 0) {
            throw new IllegalArgumentException(
                operationType + " amount must be at least " + MIN_TRANSACTION_AMOUNT);
        }
        if (amount.compareTo(MAX_TRANSACTION_AMOUNT) > 0) {
            throw new IllegalArgumentException(
                operationType + " amount cannot exceed " + MAX_TRANSACTION_AMOUNT);
        }
    }

    public void validateCustomerId(String customerId) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
        }
    }

    public void validateAccountId(String accountId) {
        if (accountId == null || accountId.trim().isEmpty()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty");
        }
    }
}