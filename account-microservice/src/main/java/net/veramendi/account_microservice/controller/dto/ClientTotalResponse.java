package net.veramendi.account_microservice.controller.dto;

import lombok.Data;

import net.veramendi.account_microservice.client.dto.ClientResponse;

import java.util.List;

@Data
public class ClientTotalResponse {

    private ClientResponse client;

    private List<AccountResponse> accounts;
}
