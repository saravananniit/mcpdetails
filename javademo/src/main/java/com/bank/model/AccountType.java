package com.bank.model;
public enum AccountType {
    SAVINGS("Savings Account", 0.03),
    CHECKING("Checking Account", 0.01),
    FIXED_DEPOSIT("Fixed Deposit", 0.06),
    MONEY_MARKET("Money Market Account", 0.04);

    private final String displayName;
    private final double interestRate;

    AccountType(String displayName, double interestRate) {
        this.displayName = displayName;
        this.interestRate = interestRate;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getInterestRate() {
        return interestRate;
    }
}
