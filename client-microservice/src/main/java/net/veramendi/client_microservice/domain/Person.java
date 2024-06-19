package net.veramendi.client_microservice.domain;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import net.veramendi.client_microservice.enums.Gender;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@MappedSuperclass
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "first_name", length = 32, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 32, nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "id_number", length = 32, nullable = false)
    private String idNumber;

    @Column(name = "address", length = 128, nullable = false)
    private String address;

    @Column(name = "phone_number", length = 32, nullable = false)
    private String phoneNumber;
}
