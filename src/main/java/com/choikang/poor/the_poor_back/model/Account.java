package com.choikang.poor.the_poor_back.model;

import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.User;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountID;

    @Column(length = 50, nullable = false)
    private String accountName;

    @Column(length = 50, nullable = false, unique = true)
    private String accountNumber;

    @Column(length = 4, nullable = false)
    private int accountPW;

    @Column(nullable = false)
    private int accountBalance;

    @Column(nullable = false)
    private boolean accountHasCan;

    @Column
    private int accountCanAmount;

    @Column
    private double accountCanInterestRate;
}
