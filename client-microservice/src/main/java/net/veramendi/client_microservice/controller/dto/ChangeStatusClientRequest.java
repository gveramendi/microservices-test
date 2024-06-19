package net.veramendi.client_microservice.controller.dto;

import lombok.Data;

@Data
public class ChangeStatusClientRequest {

    private boolean isActive;
}
