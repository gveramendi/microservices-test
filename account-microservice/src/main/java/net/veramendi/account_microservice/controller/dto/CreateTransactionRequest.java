package net.veramendi.account_microservice.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.veramendi.account_microservice.enums.TransactionType;

import java.time.LocalDateTime;

@Data
public class CreateTransactionRequest {

    @NotNull
    private double amount;

    @NotBlank
    private String description;

    @NotNull
    private TransactionType type;
}
