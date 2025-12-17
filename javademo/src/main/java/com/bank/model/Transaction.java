package com.bank.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a financial transaction in the banking system.
 * Immutable for audit trail integrity.
 */
public final class Transaction {
    private final String transactionId;
    private final String accountId;
    private final TransactionType type;
    private final BigDecimal amount;
    private final BigDecimal balanceAfter;
    private final String description;
    private final LocalDateTime timestamp;
    private final String referenceNumber;

    private Transaction(Builder builder) {
        this.transactionId = builder.transactionId;
        this.accountId = builder.accountId;
        this.type = builder.type;
        this.amount = builder.amount;
        this.balanceAfter = builder.balanceAfter;
        this.description = builder.description;
        this.timestamp = builder.timestamp;
        this.referenceNumber = builder.referenceNumber;
    }

    // Getters only - immutable
    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + transactionId + '\'' +
                ", type=" + type +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }

    public static class Builder {
        private String transactionId;
        private String accountId;
        private TransactionType type;
        private BigDecimal amount;
        private BigDecimal balanceAfter;
        private String description;
        private LocalDateTime timestamp;
        private String referenceNumber;

        public Builder() {
            this.transactionId = UUID.randomUUID().toString();
            this.timestamp = LocalDateTime.now();
            this.referenceNumber = "REF-" + System.currentTimeMillis();
        }

        public Builder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Builder accountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder type(TransactionType type) {
            this.type = type;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder balanceAfter(BigDecimal balanceAfter) {
            this.balanceAfter = balanceAfter;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder referenceNumber(String referenceNumber) {
            this.referenceNumber = referenceNumber;
            return this;
        }

        public Transaction build() {
            if (accountId == null || accountId.trim().isEmpty()) {
                throw new IllegalStateException("Account ID is required");
            }
            if (type == null) {
                throw new IllegalStateException("Transaction type is required");
            }
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalStateException("Amount must be positive");
            }
            return new Transaction(this);
        }
    }
}



