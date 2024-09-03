package com.choikang.poor.the_poor_back.dto;

import com.choikang.poor.the_poor_back.model.Account;
import lombok.*;
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private Long accountID;
    private String number;
    private String name;
    private int balance;
    private Boolean hasCan;
    private double canInterestRate;

    public static AccountDTO of(Account account) {
        return AccountDTO.builder()
                .accountID(account.getAccountID())
                .number(account.getAccountNumber())
                .name(account.getAccountName())
                .balance(account.getAccountBalance())
                .hasCan(account.isAccountHasCan())  // getHasCan -> isAccountHasCan로 수정
                .canInterestRate(account.getAccountCanInterestRate())  // 추가
                .build();
    }
}
