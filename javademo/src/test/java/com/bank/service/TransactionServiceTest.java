package com.bank.service;
import com.bank.model.Transaction;
import com.bank.model.TransactionType;
import com.bank.repository.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.Assert.*;

public class TransactionServiceTest {
    private TransactionService transactionService;
    private TransactionRepository transactionRepository;

    @Before
    public void setUp() {
        transactionRepository = new TransactionRepository();
        transactionService = new TransactionService(transactionRepository);
    }

    @Test
    public void testRecordTransaction() {
        Transaction transaction = transactionService.recordTransaction(
                "ACC-001",
                TransactionType.DEPOSIT,
                new BigDecimal("500.00"),
                new BigDecimal("1500.00"),
                "Test deposit"
        );

        assertNotNull(transaction);
        assertEquals("ACC-001", transaction.getAccountId());
        assertEquals(TransactionType.DEPOSIT, transaction.getType());
        assertEquals(new BigDecimal("500.00"), transaction.getAmount());
    }

    @Test
    public void testGetAccountTransactions() {
        transactionService.recordTransaction(
                "ACC-001",
                TransactionType.DEPOSIT,
                new BigDecimal("500.00"),
                new BigDecimal("1500.00"),
                "Deposit 1"
        );
        transactionService.recordTransaction(
                "ACC-001",
                TransactionType.WITHDRAWAL,
                new BigDecimal("200.00"),
                new BigDecimal("1300.00"),
                "Withdrawal 1"
        );

        List<Transaction> transactions = transactionService.getAccountTransactions("ACC-001");
        assertEquals(2, transactions.size());
    }

    @Test
    public void testGetTotalDeposits() {
        transactionService.recordTransaction(
                "ACC-001",
                TransactionType.DEPOSIT,
                new BigDecimal("500.00"),
                new BigDecimal("500.00"),
                "Deposit 1"
        );
        transactionService.recordTransaction(
                "ACC-001",
                TransactionType.DEPOSIT,
                new BigDecimal("300.00"),
                new BigDecimal("800.00"),
                "Deposit 2"
        );

        BigDecimal totalDeposits = transactionService.getTotalDeposits("ACC-001");
        assertEquals(new BigDecimal("800.00"), totalDeposits);
    }

    @Test
    public void testGetTransactionsByType() {
        transactionService.recordTransaction(
                "ACC-001",
                TransactionType.DEPOSIT,
                new BigDecimal("500.00"),
                new BigDecimal("500.00"),
                "Deposit"
        );
        transactionService.recordTransaction(
                "ACC-002",
                TransactionType.WITHDRAWAL,
                new BigDecimal("200.00"),
                new BigDecimal("800.00"),
                "Withdrawal"
        );

        List<Transaction> deposits = transactionService.getTransactionsByType(TransactionType.DEPOSIT);
        assertEquals(1, deposits.size());
        assertEquals(TransactionType.DEPOSIT, deposits.get(0).getType());
    }
}

