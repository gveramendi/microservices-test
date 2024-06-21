package net.veramendi.account_microservice.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.veramendi.account_microservice.controller.dto.AccountResponse;
import net.veramendi.account_microservice.controller.dto.ClientTotalResponse;
import net.veramendi.account_microservice.controller.dto.TransactionResponse;
import net.veramendi.account_microservice.domain.Account;
import net.veramendi.account_microservice.domain.Transaction;
import net.veramendi.account_microservice.service.AccountService;
import net.veramendi.account_microservice.service.CustomerService;
import net.veramendi.account_microservice.service.TransactionService;

import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
@RestController
@RequestMapping(value = "/api/reports", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReportController {

    private final AccountService accountService;

    private final TransactionService transactionService;

    private final CustomerService customerService;

    private final ModelMapper modelMapper;

    @GetMapping("/clientId/{clientId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ClientTotalResponse> getAccountsByClientId(@PathVariable String clientId,
                                                                     @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) LocalDate startDate,
                                                                     @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(required = false) LocalDate endDate) {
        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(23, 59, 59) : null;

        ClientTotalResponse clientTotalResponse = this.loadClientInfo(clientId);

        List<AccountResponse> accountResponses = new ArrayList<>();
        for (Account account : this.accountService.getByClientId(clientId)) {
            AccountResponse accountResponse = this.modelMapper.map(account, AccountResponse.class);
            accountResponse.setTransactions(this.loadTransactions(
                    account.getAccountNumber(), startDateTime, endDateTime));
            accountResponses.add(accountResponse);
        }
        clientTotalResponse.setAccounts(accountResponses);

        return ResponseEntity.status(HttpStatus.OK).body(clientTotalResponse);
    }

    private ClientTotalResponse loadClientInfo(String clientId) {
        ClientTotalResponse clientTotalResponse = new ClientTotalResponse();
        clientTotalResponse.setClient(this.customerService.checkClient(clientId));

        return clientTotalResponse;
    }

    private List<TransactionResponse> loadTransactions(String accountNumber, LocalDateTime startDate, LocalDateTime endDate) {
        List<TransactionResponse> transactions = new ArrayList<>();
        for (Transaction transaction : this.transactionService.getTransactions(accountNumber, startDate, endDate)) {
            transactions.add(this.modelMapper.map(transaction, TransactionResponse.class));
        }

        return transactions;
    }
}
