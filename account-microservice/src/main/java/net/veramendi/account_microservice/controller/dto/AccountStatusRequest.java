package net.veramendi.account_microservice.controller.dto;

import lombok.Data;
import net.veramendi.account_microservice.enums.AccountStatus;

@Data
public class AccountStatusRequest {

    private AccountStatus accountStatus;
}
