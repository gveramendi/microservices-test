package net.veramendi.account_microservice.client.dto;

import lombok.Data;

@Data
public class ClientResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String gender;

    private Integer age;

    private String idNumber;

    private String address;

    private String phoneNumber;

    private String clientId;

    private Boolean isActive;
}
