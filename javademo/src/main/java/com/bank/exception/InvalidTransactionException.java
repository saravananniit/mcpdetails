package com.bank.exception;
/**
 * Exception thrown when a transaction is invalid or cannot be processed.
 */
public class InvalidTransactionException extends RuntimeException {
    private final String reason;

    public InvalidTransactionException(String message) {
        super(message);
        this.reason = message;
    }

    public InvalidTransactionException(String message, Throwable cause) {
        super(message, cause);
        this.reason = message;
    }

    public String getReason() {
        return reason;
    }
}