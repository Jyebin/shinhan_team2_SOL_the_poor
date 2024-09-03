package com.choikang.poor.the_poor_back.dto;

import com.choikang.poor.the_poor_back.model.InstallmentSavings;
import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InstallmentSavingsDTO {

    private Long ISID;
    private String ISName;
    private String ISNumber;
    private int ISBalance;

    public static InstallmentSavingsDTO convertToDTO(InstallmentSavings installmentSavings) {
        return InstallmentSavingsDTO.builder()
                .ISID(installmentSavings.getISID())
                .ISName(installmentSavings.getISName())
                .ISNumber(installmentSavings.getISNumber())
                .ISBalance(installmentSavings.getISBalance())
                .build();
    }
}
