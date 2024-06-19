package net.veramendi.client_microservice.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import net.veramendi.client_microservice.enums.Gender;

@Data
public class CreateClientRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    private Gender gender;

    @NotNull
    private Integer age;

    @NotBlank
    private String idNumber;

    @NotBlank
    private String address;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String clientId;

    @NotBlank
    private String password;

}
