package net.veramendi.account_microservice.domain;

import jakarta.persistence.*;

import lombok.*;

import net.veramendi.account_microservice.enums.TransactionType;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Account account;

    private double amount;

    private String description;

    private LocalDateTime createdDate;

    private TransactionType type;
}
