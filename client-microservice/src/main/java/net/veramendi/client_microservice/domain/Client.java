package net.veramendi.client_microservice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "clients)")
public class Client extends Person {

    @Column(name = "client_id", unique = true, nullable = false)
    private String clientId;

    @Column(name = "password", length = 128, nullable = false)
    private String password;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}
