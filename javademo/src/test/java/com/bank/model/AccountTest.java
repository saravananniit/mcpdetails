package com.bank.model;
import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.*;

public class AccountTest {
    private Account account;

    @Before
    public void setUp() {
        account = new Account.Builder()
                .customerId("CUST-001")
                .accountType(AccountType.SAVINGS)
                .balance(new BigDecimal("1000.00"))
                .build();
    }

    @Test
    public void testAccountCreation() {
        assertNotNull(account);
        assertNotNull(account.getAccountId());
        assertEquals("CUST-001", account.getCustomerId());
        assertEquals(AccountType.SAVINGS, account.getAccountType());
        assertEquals(new BigDecimal("1000.00"), account.getBalance());
        assertTrue(account.isActive());
    }

    @Test
    public void testDeposit() {
        BigDecimal depositAmount = new BigDecimal("500.00");
        account.deposit(depositAmount);
        assertEquals(new BigDecimal("1500.00"), account.getBalance());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDepositNegativeAmount() {
        account.deposit(new BigDecimal("-100.00"));
    }

    @Test
    public void testWithdraw() {
        BigDecimal withdrawAmount = new BigDecimal("300.00");
        account.withdraw(withdrawAmount);
        assertEquals(new BigDecimal("700.00"), account.getBalance());
    }

    @Test(expected = IllegalStateException.class)
    public void testWithdrawInsufficientFunds() {
        account.withdraw(new BigDecimal("2000.00"));
    }

    @Test
    public void testAccountActivationDeactivation() {
        assertTrue(account.isActive());
        account.deactivate();
        assertFalse(account.isActive());
        account.activate();
        assertTrue(account.isActive());
    }

    @Test(expected = IllegalStateException.class)
    public void testBuilderMissingCustomerId() {
        new Account.Builder()
                .accountType(AccountType.CHECKING)
                .build();
    }
}

