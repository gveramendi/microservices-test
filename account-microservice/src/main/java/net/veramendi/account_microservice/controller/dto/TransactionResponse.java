package net.veramendi.account_microservice.controller.dto;

import lombok.Data;
import net.veramendi.account_microservice.enums.TransactionType;

import java.time.LocalDateTime;

@Data
public class TransactionResponse {

    private Long id;

    private double amount;

    private String description;

    private LocalDateTime createdDate;

    private TransactionType type;
}
