package com.bank.exception;

/**
 * Exception thrown when an account is not found in the repository.
 */
public class AccountNotFoundException extends RuntimeException {
    private final String accountId;

    public AccountNotFoundException(String accountId) {
        super("Account not found: " + accountId);
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }
}
