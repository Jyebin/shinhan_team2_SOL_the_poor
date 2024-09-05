package com.choikang.poor.the_poor_back.dto;

import lombok.*;

import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RankingResponseDTO {
    private List<RankingDTO> rankingDTOList;
    private String leagueKind;
    private int rankingUserID;
}
