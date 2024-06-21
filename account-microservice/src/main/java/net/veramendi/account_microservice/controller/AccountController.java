package net.veramendi.account_microservice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.veramendi.account_microservice.controller.dto.*;
import net.veramendi.account_microservice.domain.Account;
import net.veramendi.account_microservice.domain.Transaction;
import net.veramendi.account_microservice.service.AccountService;
import net.veramendi.account_microservice.service.CustomerService;
import net.veramendi.account_microservice.service.TransactionService;

import org.modelmapper.ModelMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = "/api/accounts", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {

    private final AccountService accountService;

    private final TransactionService transactionService;

    private final CustomerService customerService;

    private final ModelMapper modelMapper;

    @PostMapping("/{clientId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ClientTotalResponse> createAccount(@PathVariable String clientId,
                                                         @RequestBody CreateAccountRequest createAccountRequest) {
        ClientTotalResponse clientTotalResponse = this.loadClientInfo(clientId);

        Account account = this.modelMapper.map(createAccountRequest, Account.class);
        AccountResponse accountResponse = this.modelMapper.map(
                this.accountService.create(clientId, account), AccountResponse.class);
        clientTotalResponse.setAccounts(List.of(accountResponse));

        return ResponseEntity.status(HttpStatus.CREATED).body(clientTotalResponse);
    }

    @GetMapping("/clientId/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ClientTotalResponse> getAccountsByClientId(@PathVariable String clientId) {
        ClientTotalResponse clientTotalResponse = this.loadClientInfo(clientId);

        List<AccountResponse> accountResponses = new ArrayList<>();
        for (Account account : this.accountService.getByClientId(clientId)) {
            AccountResponse accountResponse = this.modelMapper.map(account, AccountResponse.class);
            accountResponse.setTransactions(this.loadTransactions(account.getAccountNumber()));
            accountResponses.add(accountResponse);
        }
        clientTotalResponse.setAccounts(accountResponses);

        return ResponseEntity.status(HttpStatus.OK).body(clientTotalResponse);
    }

    @GetMapping("/accountNumber/{accountNumber}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ClientTotalResponse> getAccountByAccountNumber(@PathVariable String accountNumber) {
        AccountResponse accountResponse =
                this.modelMapper.map(this.accountService.getByAccountNumber(accountNumber), AccountResponse.class);
        accountResponse.setTransactions(this.loadTransactions(accountNumber));

        ClientTotalResponse clientTotalResponse = this.loadClientInfo(accountResponse.getClientId());
        clientTotalResponse.setAccounts(List.of(accountResponse));

        return ResponseEntity.status(HttpStatus.OK).body(clientTotalResponse);
    }

    @PatchMapping("/accountNumber/{accountNumber}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ClientTotalResponse> updateAccount(@PathVariable String accountNumber,
                                                             @RequestBody AccountStatusRequest accountStatusRequest) {
        AccountResponse accountResponse =
                this.modelMapper.map(
                        this.accountService.updateStatus(accountNumber, accountStatusRequest.getAccountStatus()),
                        AccountResponse.class);
        accountResponse.setTransactions(this.loadTransactions(accountNumber));

        ClientTotalResponse clientTotalResponse = this.loadClientInfo(accountResponse.getClientId());
        clientTotalResponse.setAccounts(List.of(accountResponse));

        return ResponseEntity.status(HttpStatus.OK).body(clientTotalResponse);
    }

    private ClientTotalResponse loadClientInfo(String clientId) {
        ClientTotalResponse clientTotalResponse = new ClientTotalResponse();
        clientTotalResponse.setClient(this.customerService.checkClient(clientId));

        return clientTotalResponse;
    }



    private List<TransactionResponse> loadTransactions(String accountNumber) {
        List<TransactionResponse> transactions = new ArrayList<>();
        for (Transaction transaction : this.transactionService.getTransactionsByAccountNumber(accountNumber)) {
            transactions.add(this.modelMapper.map(transaction, TransactionResponse.class));
        }

        return transactions;
    }
}
