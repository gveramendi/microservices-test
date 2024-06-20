package net.veramendi.account_microservice.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.veramendi.account_microservice.enums.AccountType;

@Data
public class CreateAccountRequest {

    @NotBlank
    private String accountNumber;

    @NotNull
    private double initialBalance;

    @NotNull
    private AccountType accountType;
}
