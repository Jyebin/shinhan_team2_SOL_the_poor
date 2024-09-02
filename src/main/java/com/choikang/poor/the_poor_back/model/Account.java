package com.choikang.poor.the_poor_back.model;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Setter  // 이 부분을 추가합니다.
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

    @ManyToOne
    @JoinColumn(name="userID", nullable = false)
    private User user;

    // Getter와 Setter가 자동으로 생성됩니다. (Lombok의 @Getter와 @Setter 어노테이션 덕분에)
}
