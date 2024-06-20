package net.veramendi.account_microservice.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

import lombok.*;

import net.veramendi.account_microservice.enums.AccountStatus;
import net.veramendi.account_microservice.enums.AccountType;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String clientId;

    private String accountNumber;

    private double initialBalance;

    private double currentBalance;

    @Enumerated(value = EnumType.STRING)
    private AccountType accountType;

    @Enumerated(value = EnumType.STRING)
    private AccountStatus status;

    private LocalDateTime createdDate;

    @JsonBackReference
    @OneToMany(
            mappedBy = "account",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    private Set<Transaction> transactions;
}
