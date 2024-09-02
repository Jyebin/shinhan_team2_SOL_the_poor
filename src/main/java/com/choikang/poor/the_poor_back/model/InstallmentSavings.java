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
 
}
