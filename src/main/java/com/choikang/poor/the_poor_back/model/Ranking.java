package com.choikang.poor.the_poor_back.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Ranking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rankingID;

    @Column(unique = true)
    private Long rankingUserID;

    @Column
    private String rankingUserName;

    @Column
    private int rankingScore;

    @Column
    private int userTotalScore;

    @ColumnDefault("1")
    private int rankingLeagueKind;
}
