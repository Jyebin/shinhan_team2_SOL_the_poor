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

    public static AccountDTO convertToDTO(Account account) {
        return AccountDTO.builder()
                .accountID(account.getAccountID())
                .number(account.getAccountNumber())
                .name(account.getAccountName())
                .balance(account.getAccountBalance())
                .hasCan(account.isAccountHasCan())
                .build();
    }
}
