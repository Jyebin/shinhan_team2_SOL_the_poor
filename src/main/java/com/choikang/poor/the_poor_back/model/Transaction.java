package com.choikang.poor.the_poor_back.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionID;

    @Column(nullable = false)
    private int transactionMoney;

    @Column(length = 10, nullable = false)
    private String transactionName;

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    @Column(nullable = false)
    private Boolean transactionIsDeposit;

    @Column(nullable = false)
    private int transactionBalance;

    @ManyToOne
    @JoinColumn(name="accountID", nullable = false)
    private Account account;
}