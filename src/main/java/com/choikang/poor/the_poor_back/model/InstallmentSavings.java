package com.choikang.poor.the_poor_back.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InstallmentSavings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ISID;

    @Column(length = 50, nullable = false)
    private String ISName;

    @Column(length = 50, nullable = false, unique = true)
    private String ISNumber;

    @Column(length = 4, nullable = false)
    private int ISPW;

    @Column(nullable = false)
    private int ISBalance;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;
}
