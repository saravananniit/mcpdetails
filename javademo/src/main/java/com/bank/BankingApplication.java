package com.bank;

/**
 * Hello world!
 *
 */
import com.bank.model.*;
import com.bank.repository.*;
import com.bank.service.*;
import com.bank.util.CurrencyFormatter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Main application class for Banking Management System.
 * Demonstrates Java 8 features and best practices.
 */
public class BankingApplication {
    private static final Logger LOGGER = Logger.getLogger(BankingApplication.class.getName());
    
    private final AccountService accountService;
    private final CustomerService customerService;
    private final TransactionService transactionService;

    public BankingApplication() {
        // Initialize repositories
        AccountRepository accountRepository = new AccountRepository();
        CustomerRepository customerRepository = new CustomerRepository();
        TransactionRepository transactionRepository = new TransactionRepository();

        // Initialize services
        ValidationService validationService = new ValidationService();
        this.transactionService = new TransactionService(transactionRepository);
        this.accountService = new AccountService(accountRepository, transactionService, validationService);
        this.customerService = new CustomerService(customerRepository, validationService);
    }

    public static void main(String[] args) {
        LOGGER.info("Starting Banking Management System");
        
        BankingApplication app = new BankingApplication();
        app.runDemo(args);
        
        LOGGER.info("Banking Management System stopped");
    }

    private void runDemo(String[] args) {
        System.out.println("===========================================");
        System.out.println("  BANKING MANAGEMENT SYSTEM - JAVA 8");
        System.out.println("===========================================\n");

        try {
            // Create customers
            System.out.println("1. Creating Customers...");
            Customer customer1 = createSampleCustomer("John", "Doe", "john.doe@email.com");
            Customer customer2 = createSampleCustomer("Jane", "Smith", "jane.smith@email.com");
            System.out.println("   Created: " + customer1.getFullName());
            System.out.println("   Created: " + customer2.getFullName());
            System.out.println();

            // Create accounts
            System.out.println("2. Creating Accounts...");
            Account savingsAccount = accountService.createAccount(
                customer1.getCustomerId(), 
                AccountType.SAVINGS, 
                new BigDecimal("5000.00")
            );
            Account checkingAccount = accountService.createAccount(
                customer1.getCustomerId(), 
                AccountType.CHECKING, 
                new BigDecimal("2000.00")
            );
            Account customer2Account = accountService.createAccount(
                customer2.getCustomerId(), 
                AccountType.SAVINGS, 
                new BigDecimal("3000.00")
            );
            
            displayAccount(savingsAccount);
            displayAccount(checkingAccount);
            displayAccount(customer2Account);
            System.out.println();

            // Perform transactions
            System.out.println("3. Performing Transactions...");
            
            System.out.println("   Depositing $500 to savings account...");
            accountService.deposit(savingsAccount.getAccountId(), new BigDecimal("500.00"), "Salary deposit");
            displayBalance(savingsAccount.getAccountId());
            
            System.out.println("   Withdrawing $200 from checking account...");
            accountService.withdraw(checkingAccount.getAccountId(), new BigDecimal("200.00"), "ATM withdrawal");
            displayBalance(checkingAccount.getAccountId());
            
            System.out.println("   Transferring $1000 from savings to checking...");
            accountService.transfer(
                savingsAccount.getAccountId(), 
                checkingAccount.getAccountId(), 
                new BigDecimal("1000.00")
            );
            displayBalance(savingsAccount.getAccountId());
            displayBalance(checkingAccount.getAccountId());
            System.out.println();

            // Apply interest
            System.out.println("4. Applying Interest...");
            accountService.applyInterest(savingsAccount.getAccountId());
            displayBalance(savingsAccount.getAccountId());
            System.out.println();

            // Display transaction history
            System.out.println("5. Transaction History for Savings Account:");
            displayTransactionHistory(savingsAccount.getAccountId());
            System.out.println();

            // Display statistics
            System.out.println("6. System Statistics:");
            displayStatistics();
            System.out.println();

            // Demonstrate Java 8 Stream operations
            System.out.println("7. Java 8 Features Demo:");
            demonstrateJava8Features();
            System.out.println();

            // Interactive menu (optional)
            if (args.length > 0 && args[0].equals("--interactive")) {
                runInteractiveMode();
            }

            System.out.println("===========================================");
            System.out.println("  Demo completed successfully!");
            System.out.println("===========================================");

        } catch (Exception e) {
            LOGGER.severe("Error during demo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Customer createSampleCustomer(String firstName, String lastName, String email) {
        return customerService.createCustomer(
            firstName,
            lastName,
            email,
            "+1234567890",
            LocalDate.of(1990, 1, 15),
            "123 Main St, City, State 12345"
        );
    }

    private void displayAccount(Account account) {
        System.out.println("   Account ID: " + account.getAccountId());
        System.out.println("   Type: " + account.getAccountType().getDisplayName());
        System.out.println("   Balance: " + CurrencyFormatter.formatUSD(account.getBalance()));
    }

    private void displayBalance(String accountId) {
        BigDecimal balance = accountService.getBalance(accountId);
        System.out.println("   Current Balance: " + CurrencyFormatter.formatUSD(balance));
    }

    private void displayTransactionHistory(String accountId) {
        List<Transaction> transactions = transactionService.getAccountTransactions(accountId);
        transactions.forEach(transaction -> {
            System.out.println("   " + transaction.getTimestamp() + " | " +
                             transaction.getType().getDisplayName() + " | " +
                             CurrencyFormatter.formatUSD(transaction.getAmount()) + " | " +
                             "Balance: " + CurrencyFormatter.formatUSD(transaction.getBalanceAfter()));
        });
    }

    private void displayStatistics() {
        long totalCustomers = customerService.getTotalCustomerCount();
        List<Account> allAccounts = accountService.getAllAccounts();
        
        BigDecimal totalBalance = allAccounts.stream()
            .map(Account::getBalance)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        long activeAccounts = allAccounts.stream()
            .filter(Account::isActive)
            .count();

        System.out.println("   Total Customers: " + totalCustomers);
        System.out.println("   Total Accounts: " + allAccounts.size());
        System.out.println("   Active Accounts: " + activeAccounts);
        System.out.println("   Total Balance: " + CurrencyFormatter.formatUSD(totalBalance));
    }

    private void demonstrateJava8Features() {
        List<Account> accounts = accountService.getAllAccounts();
        
        // Stream operations
        System.out.println("   High-value accounts (balance > $4000):");
        accounts.stream()
            .filter(account -> account.getBalance().compareTo(new BigDecimal("4000")) > 0)
            .forEach(account -> System.out.println("     - " + account.getAccountId() + 
                    ": " + CurrencyFormatter.formatUSD(account.getBalance())));
        
        // Lambda expressions
        System.out.println("\n   Account types distribution:");
        accounts.stream()
            .collect(java.util.stream.Collectors.groupingBy(
                Account::getAccountType,
                java.util.stream.Collectors.counting()
            ))
            .forEach((type, count) -> 
                System.out.println("     - " + type.getDisplayName() + ": " + count)
            );
        
        // Optional usage
        System.out.println("\n   Average balance: " + 
            accounts.stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(accounts.size()), 2, java.math.RoundingMode.HALF_UP)
        );
    }

    private void runInteractiveMode() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n--- Interactive Menu ---");
            System.out.println("1. Create Customer");
            System.out.println("2. Create Account");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Transfer");
            System.out.println("6. View Account");
            System.out.println("7. View Transactions");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 0:
                        running = false;
                        System.out.println("Exiting...");
                        break;
                    case 1:
                        // Create customer logic
                        System.out.println("Customer creation - implementation pending");
                        break;
                    // Add other cases as needed
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }
}

