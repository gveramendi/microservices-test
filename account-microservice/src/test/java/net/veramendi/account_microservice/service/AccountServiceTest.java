package net.veramendi.account_microservice.service;

import net.veramendi.account_microservice.domain.Account;
import net.veramendi.account_microservice.enums.AccountStatus;
import net.veramendi.account_microservice.enums.TransactionType;
import net.veramendi.account_microservice.exception.ResourceAlreadyExistsException;
import net.veramendi.account_microservice.exception.ResourceNotFoundException;
import net.veramendi.account_microservice.repository.AccountRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateAccountSuccessfully() {
        Account newAccount = new Account();
        newAccount.setAccountNumber("0010010");
        when(accountRepository.findByAccountNumber(newAccount.getAccountNumber())).thenReturn(Optional.empty());
        when(accountRepository.save(newAccount)).thenReturn(newAccount);

        Account createdAccount = accountService.create("00222001", newAccount);

        assertNotNull(createdAccount);
        assertEquals("00222001", createdAccount.getClientId());
        assertEquals(AccountStatus.ACTIVE, createdAccount.getStatus());
        assertEquals(newAccount.getInitialBalance(), createdAccount.getCurrentBalance());
    }

    @Test
    void shouldNotCreateAccountWithExistingAccountNumber() {
        Account existingAccount = new Account();
        existingAccount.setAccountNumber("0010010");
        when(accountRepository.findByAccountNumber(existingAccount.getAccountNumber())).thenReturn(Optional.of(existingAccount));

        Account newAccount = new Account();
        newAccount.setAccountNumber("0010010");

        assertThrows(ResourceAlreadyExistsException.class, () -> accountService.create("clientId", newAccount));
    }

    @Test
    void shouldUpdateAccountBalanceWithDeposit() {
        Account existingAccount = new Account();
        existingAccount.setAccountNumber("0010010");
        existingAccount.setCurrentBalance(100.0);
        when(accountRepository.findByAccountNumber(existingAccount.getAccountNumber())).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(existingAccount)).thenReturn(existingAccount);

        Account updatedAccount = accountService.update(existingAccount, 50.0, TransactionType.DEPOSIT);

        assertEquals(150.0, updatedAccount.getCurrentBalance());
        assertEquals(AccountStatus.ACTIVE, updatedAccount.getStatus());
    }

    @Test
    void shouldUpdateAccountBalanceWithWithdrawal() {
        Account existingAccount = new Account();
        existingAccount.setAccountNumber("0010010");
        existingAccount.setCurrentBalance(100.0);
        when(accountRepository.findByAccountNumber(existingAccount.getAccountNumber())).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(existingAccount)).thenReturn(existingAccount);

        Account updatedAccount = accountService.update(existingAccount, 50.0, TransactionType.WITHDRAWAL);

        assertEquals(50.0, updatedAccount.getCurrentBalance());
        assertEquals(AccountStatus.ACTIVE, updatedAccount.getStatus());
    }

    @Test
    void shouldUpdateAccountStatusToHoldWhenBalanceIsZero() {
        Account existingAccount = new Account();
        existingAccount.setAccountNumber("0010010");
        existingAccount.setCurrentBalance(100.0);
        when(accountRepository.findByAccountNumber(existingAccount.getAccountNumber())).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(existingAccount)).thenReturn(existingAccount);

        Account updatedAccount = accountService.update(existingAccount, 100.0, TransactionType.WITHDRAWAL);

        assertEquals(0.0, updatedAccount.getCurrentBalance());
        assertEquals(AccountStatus.HOLD, updatedAccount.getStatus());
    }

    @Test
    void shouldUpdateAccountStatusSuccessfully() {
        Account existingAccount = new Account();
        existingAccount.setAccountNumber("0010010");
        existingAccount.setStatus(AccountStatus.ACTIVE);
        when(accountRepository.findByAccountNumber(existingAccount.getAccountNumber())).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(existingAccount)).thenReturn(existingAccount);

        Account updatedAccount = accountService.updateStatus("0010010", AccountStatus.HOLD);

        assertEquals(AccountStatus.HOLD, updatedAccount.getStatus());
    }

    @Test
    void shouldNotUpdateAccountStatusForNonExistingAccount() {
        when(accountRepository.findByAccountNumber("0010010")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.updateStatus("0010010", AccountStatus.HOLD));
    }
}
