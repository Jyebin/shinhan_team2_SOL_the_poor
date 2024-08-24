package com.choikang.poor.the_poor_back.dto;

import com.choikang.poor.the_poor_back.model.Account;
import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private String number;
    private String name;
    private int balance;
    private Boolean hasCan;
}
