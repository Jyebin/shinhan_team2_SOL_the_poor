package com.choikang.poor.the_poor_back.dto;

import com.choikang.poor.the_poor_back.model.Ranking;
import com.choikang.poor.the_poor_back.model.Transaction;
import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RankingDTO {
    private int rankingNum;
    private int rankingUserNum;
    private String rankingUserName;
    private int rankingScore;
    private int userTotalScore;
}
