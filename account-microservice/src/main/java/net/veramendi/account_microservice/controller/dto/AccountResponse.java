package net.veramendi.account_microservice.controller.dto;

import lombok.Data;

import net.veramendi.account_microservice.enums.AccountStatus;
import net.veramendi.account_microservice.enums.AccountType;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AccountResponse {

    private Long id;

    private String clientId;

    private String accountNumber;

    private double initialBalance;

    private double currentBalance;

    private AccountType accountType;

    private AccountStatus status;

    private LocalDateTime createdDate;

    private List<TransactionResponse> transactions;
}
