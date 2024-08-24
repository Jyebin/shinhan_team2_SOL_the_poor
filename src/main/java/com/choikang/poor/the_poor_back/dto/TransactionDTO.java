package com.choikang.poor.the_poor_back.dto;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private String date;
    private String time;
    private String description;
    private String status;
    private int amount;
    private int balance;
}
