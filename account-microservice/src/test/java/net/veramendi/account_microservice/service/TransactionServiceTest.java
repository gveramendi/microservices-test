package net.veramendi.account_microservice.service;

import net.veramendi.account_microservice.domain.Account;
import net.veramendi.account_microservice.domain.Transaction;
import net.veramendi.account_microservice.enums.AccountStatus;
import net.veramendi.account_microservice.enums.TransactionType;
import net.veramendi.account_microservice.exception.ResourceNotFoundException;
import net.veramendi.account_microservice.exception.TransactionNotValidException;
import net.veramendi.account_microservice.repository.TransactionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TransactionServiceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldNotCreateTransactionWhenAccountIsHold() {
        Account account = new Account();
        account.setAccountNumber("0010010");
        account.setStatus(AccountStatus.HOLD);
        when(accountService.getByAccountNumber(account.getAccountNumber())).thenReturn(account);

        Transaction newTransaction = new Transaction();

        assertThrows(TransactionNotValidException.class, () -> transactionService.create(account.getAccountNumber(), newTransaction));
    }

    @Test
    void shouldNotCreateTransactionWhenWithdrawalExceedsBalance() {
        Account account = new Account();
        account.setAccountNumber("0010010");
        account.setStatus(AccountStatus.ACTIVE);
        account.setCurrentBalance(100.0);
        when(accountService.getByAccountNumber(account.getAccountNumber())).thenReturn(account);

        Transaction newTransaction = new Transaction();
        newTransaction.setType(TransactionType.WITHDRAWAL);
        newTransaction.setAmount(150.0);

        assertThrows(TransactionNotValidException.class, () -> transactionService.create(account.getAccountNumber(), newTransaction));
    }

    @Test
    void shouldGetTransactionsByAccountNumberSuccessfully() {
        Account account = new Account();
        account.setAccountNumber("0010010");
        when(accountService.getByAccountNumber(account.getAccountNumber())).thenReturn(account);

        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction = new Transaction();
        transactions.add(transaction);
        when(transactionRepository.getAllByAccountId(account.getId())).thenReturn(transactions);

        List<Transaction> retrievedTransactions = transactionService.getTransactionsByAccountNumber("0010010");

        assertFalse(retrievedTransactions.isEmpty());
        assertEquals(transactions, retrievedTransactions);
    }

    @Test
    void shouldNotGetTransactionsByNonExistingAccountNumber() {
        when(accountService.getByAccountNumber("0010010")).thenThrow(new ResourceNotFoundException("Account not found"));

        assertThrows(ResourceNotFoundException.class, () -> transactionService.getTransactionsByAccountNumber("0010010"));
    }
}
