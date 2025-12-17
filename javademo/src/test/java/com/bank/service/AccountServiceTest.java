package com.bank.service;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InsufficientFundsException;
import com.bank.model.Account;
import com.bank.model.AccountType;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.*;

public class AccountServiceTest {
    private AccountService accountService;
    private AccountRepository accountRepository;
    private TransactionService transactionService;
    private ValidationService validationService;

    @Before
    public void setUp() {
        accountRepository = new AccountRepository();
        TransactionRepository transactionRepository = new TransactionRepository();
        transactionService = new TransactionService(transactionRepository);
        validationService = new ValidationService();
        accountService = new AccountService(accountRepository, transactionService, validationService);
    }

    @Test
    public void testCreateAccount() {
        Account account = accountService.createAccount(
                "CUST-001",
                AccountType.SAVINGS,
                new BigDecimal("1000.00")
        );

        assertNotNull(account);
        assertEquals("CUST-001", account.getCustomerId());
        assertEquals(AccountType.SAVINGS, account.getAccountType());
        assertEquals(new BigDecimal("1000.00"), account.getBalance());
    }

    @Test
    public void testDeposit() {
        Account account = accountService.createAccount(
                "CUST-001",
                AccountType.CHECKING,
                new BigDecimal("500.00")
        );

        accountService.deposit(account.getAccountId(), new BigDecimal("200.00"), "Test deposit");
        
        Account updated = accountService.getAccount(account.getAccountId());
        assertEquals(new BigDecimal("700.00"), updated.getBalance());
    }

    @Test
    public void testWithdraw() {
        Account account = accountService.createAccount(
                "CUST-001",
                AccountType.CHECKING,
                new BigDecimal("500.00")
        );

        accountService.withdraw(account.getAccountId(), new BigDecimal("200.00"), "Test withdrawal");
        
        Account updated = accountService.getAccount(account.getAccountId());
        assertEquals(new BigDecimal("300.00"), updated.getBalance());
    }

    @Test(expected = InsufficientFundsException.class)
    public void testWithdrawInsufficientFunds() {
        Account account = accountService.createAccount(
                "CUST-001",
                AccountType.CHECKING,
                new BigDecimal("100.00")
        );

        accountService.withdraw(account.getAccountId(), new BigDecimal("200.00"), "Test");
    }

    @Test
    public void testTransfer() {
        Account account1 = accountService.createAccount(
                "CUST-001",
                AccountType.SAVINGS,
                new BigDecimal("1000.00")
        );
        Account account2 = accountService.createAccount(
                "CUST-002",
                AccountType.CHECKING,
                new BigDecimal("500.00")
        );

        accountService.transfer(
                account1.getAccountId(),
                account2.getAccountId(),
                new BigDecimal("300.00")
        );

        Account updated1 = accountService.getAccount(account1.getAccountId());
        Account updated2 = accountService.getAccount(account2.getAccountId());

        assertEquals(new BigDecimal("700.00"), updated1.getBalance());
        assertEquals(new BigDecimal("800.00"), updated2.getBalance());
    }

    @Test(expected = AccountNotFoundException.class)
    public void testGetNonExistentAccount() {
        accountService.getAccount("NON-EXISTENT");
    }

    @Test
    public void testApplyInterest() {
        Account account = accountService.createAccount(
                "CUST-001",
                AccountType.SAVINGS,
                new BigDecimal("1000.00")
        );

        accountService.applyInterest(account.getAccountId());
        
        Account updated = accountService.getAccount(account.getAccountId());
        // SAVINGS has 3% interest rate
        BigDecimal expectedBalance = new BigDecimal("1000.00")
                .add(new BigDecimal("1000.00").multiply(BigDecimal.valueOf(0.03)));
        assertEquals(expectedBalance, updated.getBalance());
    }

    @Test
    public void testDeactivateAccount() {
        Account account = accountService.createAccount(
                "CUST-001",
                AccountType.SAVINGS,
                new BigDecimal("1000.00")
        );

        assertTrue(account.isActive());
        accountService.deactivateAccount(account.getAccountId());
        
        Account updated = accountService.getAccount(account.getAccountId());
        assertFalse(updated.isActive());
    }
}


