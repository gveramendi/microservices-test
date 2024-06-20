package net.veramendi.account_microservice.service;

import jakarta.transaction.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.veramendi.account_microservice.domain.Account;
import net.veramendi.account_microservice.domain.Transaction;
import net.veramendi.account_microservice.enums.AccountStatus;
import net.veramendi.account_microservice.enums.TransactionType;
import net.veramendi.account_microservice.exception.TransactionNotValidException;
import net.veramendi.account_microservice.repository.TransactionRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final AccountService accountService;

    @Transactional
    public Transaction create(String accountNumber, Transaction newTransaction) {
        Account account = this.accountService.getByAccountNumber(accountNumber);
        Transaction transaction = null;

        if (account.getStatus() == AccountStatus.HOLD) {
            String message = "Account %s is HOLD.".formatted(account.getAccountNumber());

            log.error(message);
            throw new TransactionNotValidException(message);
        }

        if (newTransaction.getType() == TransactionType.WITHDRAWAL
                && account.getCurrentBalance() < newTransaction.getAmount()) {
            String message = "Account %s does not have enough balance.".formatted(account.getAccountNumber());

            log.error(message);
            throw new TransactionNotValidException(message);
        }

        return createTransaction(account, newTransaction);
    }

    public List<Transaction> getTransactionsByAccountNumber(String accountNumber) {
        Account account = this.accountService.getByAccountNumber(accountNumber);

        return this.transactionRepository.getAllByAccountId(account.getId());
    }

    private Transaction createTransaction(Account account, Transaction newTransaction) {
        this.accountService.update(account, newTransaction.getAmount(), newTransaction.getType());

        newTransaction.setAccount(account);
        newTransaction.setCreatedDate(LocalDateTime.now());

        Transaction createdTransaction = this.transactionRepository.save(newTransaction);
        log.info("Transaction with id {} was created.", createdTransaction.getId());

        return createdTransaction;
    }
}
