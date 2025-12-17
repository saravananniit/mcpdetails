package com.bank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a bank account in the system.
 * Implements best practices: immutability where possible, proper encapsulation.
 */
public class Account {
    private final String accountId;
    private final String customerId;
    private final AccountType accountType;
    private BigDecimal balance;
    private final LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private boolean isActive;

    // Private constructor to enforce builder pattern
    private Account(Builder builder) {
        this.accountId = builder.accountId;
        this.customerId = builder.customerId;
        this.accountType = builder.accountType;
        this.balance = builder.balance;
        this.createdAt = builder.createdAt;
        this.lastModifiedAt = builder.lastModifiedAt;
        this.isActive = builder.isActive;
    }

    // Getters
    public String getAccountId() {
        return accountId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public boolean isActive() {
        return isActive;
    }

    // Business methods
    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        this.balance = this.balance.add(amount);
        this.lastModifiedAt = LocalDateTime.now();
    }

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }
        this.balance = this.balance.subtract(amount);
        this.lastModifiedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.isActive = false;
        this.lastModifiedAt = LocalDateTime.now();
    }

    public void activate() {
        this.isActive = true;
        this.lastModifiedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountId, account.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", accountType=" + accountType +
                ", balance=" + balance +
                ", isActive=" + isActive +
                '}';
    }

    // Builder Pattern for flexible object creation
    public static class Builder {
        private String accountId;
        private String customerId;
        private AccountType accountType;
        private BigDecimal balance;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private boolean isActive;

        public Builder() {
            this.accountId = UUID.randomUUID().toString();
            this.balance = BigDecimal.ZERO;
            this.createdAt = LocalDateTime.now();
            this.lastModifiedAt = LocalDateTime.now();
            this.isActive = true;
        }

        public Builder accountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder accountType(AccountType accountType) {
            this.accountType = accountType;
            return this;
        }

        public Builder balance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder lastModifiedAt(LocalDateTime lastModifiedAt) {
            this.lastModifiedAt = lastModifiedAt;
            return this;
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Account build() {
            if (customerId == null || customerId.trim().isEmpty()) {
                throw new IllegalStateException("Customer ID is required");
            }
            if (accountType == null) {
                throw new IllegalStateException("Account type is required");
            }
            return new Account(this);
        }
    }
}


