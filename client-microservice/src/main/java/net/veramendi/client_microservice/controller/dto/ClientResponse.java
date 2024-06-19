package net.veramendi.client_microservice.controller.dto;

import lombok.Data;
import net.veramendi.client_microservice.enums.Gender;

@Data
public class ClientResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private Gender gender;

    private Integer age;

    private String idNumber;

    private String address;

    private String phoneNumber;

    private String clientId;

    private Boolean isActive;
}
