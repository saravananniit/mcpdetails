package com.bank.service;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InsufficientFundsException;
import com.bank.exception.InvalidTransactionException;
import com.bank.model.*;
import com.bank.repository.AccountRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service layer for account operations.
 * Implements business logic and validation.
 */
public class AccountService {
    private static final Logger LOGGER = Logger.getLogger(AccountService.class.getName());
    
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    private final ValidationService validationService;

    public AccountService(AccountRepository accountRepository,
                         TransactionService transactionService,
                         ValidationService validationService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
        this.validationService = validationService;
    }

    public Account createAccount(String customerId, AccountType accountType, BigDecimal initialDeposit) {
        LOGGER.info(String.format("Creating account for customer %s", customerId));
        
        validationService.validateCustomerId(customerId);
        validationService.validateAmount(initialDeposit, "Initial deposit");

        Account account = new Account.Builder()
                .customerId(customerId)
                .accountType(accountType)
                .balance(initialDeposit)
                .build();

        Account savedAccount = accountRepository.save(account);
        
        if (initialDeposit.compareTo(BigDecimal.ZERO) > 0) {
            transactionService.recordTransaction(
                savedAccount.getAccountId(),
                TransactionType.DEPOSIT,
                initialDeposit,
                savedAccount.getBalance(),
                "Initial deposit"
            );
        }

        LOGGER.info(String.format("Account created: %s", savedAccount.getAccountId()));
        return savedAccount;
    }

    public Account getAccount(String accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    public List<Account> getCustomerAccounts(String customerId) {
        return accountRepository.findByCustomerId(customerId);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public void deposit(String accountId, BigDecimal amount, String description) {
        LOGGER.info(String.format("Processing deposit: %s to account %s", amount, accountId));
        
        validationService.validateAmount(amount, "Deposit");
        Account account = getAccount(accountId);
        
        if (!account.isActive()) {
            throw new InvalidTransactionException("Cannot deposit to inactive account");
        }

        account.deposit(amount);
        accountRepository.save(account);
        
        transactionService.recordTransaction(
            accountId,
            TransactionType.DEPOSIT,
            amount,
            account.getBalance(),
            description != null ? description : "Deposit"
        );

        LOGGER.info("Deposit completed successfully");
    }

    public void withdraw(String accountId, BigDecimal amount, String description) {
        LOGGER.info(String.format("Processing withdrawal: %s from account %s", amount, accountId));
        
        validationService.validateAmount(amount, "Withdrawal");
        Account account = getAccount(accountId);
        
        if (!account.isActive()) {
            throw new InvalidTransactionException("Cannot withdraw from inactive account");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(accountId, amount, account.getBalance());
        }

        account.withdraw(amount);
        accountRepository.save(account);
        
        transactionService.recordTransaction(
            accountId,
            TransactionType.WITHDRAWAL,
            amount,
            account.getBalance(),
            description != null ? description : "Withdrawal"
        );

        LOGGER.info("Withdrawal completed successfully");
    }

    public void transfer(String fromAccountId, String toAccountId, BigDecimal amount) {
        LOGGER.info(String.format("Processing transfer: %s from %s to %s", 
                    amount, fromAccountId, toAccountId));
        
        validationService.validateAmount(amount, "Transfer");
        
        if (fromAccountId.equals(toAccountId)) {
            throw new InvalidTransactionException("Cannot transfer to the same account");
        }

        Account fromAccount = getAccount(fromAccountId);
        Account toAccount = getAccount(toAccountId);

        if (!fromAccount.isActive() || !toAccount.isActive()) {
            throw new InvalidTransactionException("Both accounts must be active for transfer");
        }

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(fromAccountId, amount, fromAccount.getBalance());
        }

        fromAccount.withdraw(amount);
        toAccount.deposit(amount);
        
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        String referenceNumber = "TRF-" + System.currentTimeMillis();
        
        transactionService.recordTransaction(
            fromAccountId,
            TransactionType.TRANSFER,
            amount,
            fromAccount.getBalance(),
            "Transfer to " + toAccountId + " - Ref: " + referenceNumber
        );
        
        transactionService.recordTransaction(
            toAccountId,
            TransactionType.TRANSFER,
            amount,
            toAccount.getBalance(),
            "Transfer from " + fromAccountId + " - Ref: " + referenceNumber
        );

        LOGGER.info("Transfer completed successfully");
    }

    public BigDecimal getBalance(String accountId) {
        return getAccount(accountId).getBalance();
    }

    public void deactivateAccount(String accountId) {
        Account account = getAccount(accountId);
        account.deactivate();
        accountRepository.save(account);
        LOGGER.info(String.format("Account %s deactivated", accountId));
    }

    public void activateAccount(String accountId) {
        Account account = getAccount(accountId);
        account.activate();
        accountRepository.save(account);
        LOGGER.info(String.format("Account %s activated", accountId));
    }

    public void applyInterest(String accountId) {
        Account account = getAccount(accountId);
        double interestRate = account.getAccountType().getInterestRate();
        BigDecimal interest = account.getBalance()
                .multiply(BigDecimal.valueOf(interestRate));
        
        if (interest.compareTo(BigDecimal.ZERO) > 0) {
            account.deposit(interest);
            accountRepository.save(account);
            
            transactionService.recordTransaction(
                accountId,
                TransactionType.INTEREST,
                interest,
                account.getBalance(),
                "Interest credit at " + (interestRate * 100) + "%"
            );
            
            LOGGER.info(String.format("Interest applied: %s to account %s", interest, accountId));
        }
    }
}

