package net.veramendi.account_microservice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.veramendi.account_microservice.domain.Account;
import net.veramendi.account_microservice.enums.AccountStatus;
import net.veramendi.account_microservice.enums.TransactionType;
import net.veramendi.account_microservice.exception.ResourceAlreadyExistsException;
import net.veramendi.account_microservice.exception.ResourceNotFoundException;
import net.veramendi.account_microservice.repository.AccountRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public Account create(String clientId, Account newAccount) {
        // Check if account already exists
        Optional<Account> account = this.accountRepository.findByAccountNumber(newAccount.getAccountNumber());
        if (account.isPresent()) {
            String message = "Account %s already exists.".formatted(newAccount.getAccountNumber());

            log.error(message);
            throw new ResourceAlreadyExistsException(message);
        }

        // Set account properties
        newAccount.setClientId(clientId);
        newAccount.setStatus(AccountStatus.ACTIVE);
        newAccount.setCurrentBalance(newAccount.getInitialBalance());
        newAccount.setCreatedDate(LocalDateTime.now());

        // Save account
        Account createdAccount = this.accountRepository.save(newAccount);
        log.info("Account with id {} was created.", createdAccount.getId());

        return createdAccount;
    }

    public List<Account> getByClientId(String clientId) {
        return this.accountRepository.findByClientId(clientId);
    }

    public Account getByAccountNumber(String accountNumber) {
        Optional<Account> account = this.accountRepository.findByAccountNumber(accountNumber);
        if (account.isEmpty()) {
            String message = "Account %s does not exist.".formatted(accountNumber);

            log.error(message);
            throw new ResourceNotFoundException(message);
        }

        return account.get();
    }

    public Account update(Account account, double amount, TransactionType transactionType) {
        if (transactionType == TransactionType.DEPOSIT) {
            account.setCurrentBalance(account.getCurrentBalance() + amount);
        } else if (transactionType == TransactionType.WITHDRAWAL) {
            account.setCurrentBalance(account.getCurrentBalance() - amount);
        }

        account.setStatus((account.getCurrentBalance() > 0) ? AccountStatus.ACTIVE : AccountStatus.HOLD);

        Account updatedAccount = this.accountRepository.save(account);
        log.info("Account with id {} was updated.", updatedAccount.getId());

        return updatedAccount;
    }

    public Account updateStatus(String accountNumber, AccountStatus accountStatus) {
        Account account = this.getByAccountNumber(accountNumber);

        account.setStatus(accountStatus);

        Account updatedAccount = this.accountRepository.save(account);
        log.info("Status account with id {} was updated.", updatedAccount.getId());

        return updatedAccount;
    }
}
