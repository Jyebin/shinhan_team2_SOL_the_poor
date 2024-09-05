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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    @Column(length = 20, nullable = false)
    private String userName;

    @Column(length = 30, nullable = false)
    private String userEmail;

    @Column
    private int userAttendanceCnt;

    @Column(nullable = false)
    private boolean userHasCan;

    @ColumnDefault("1")
    private int userLeagueKind;

    @Column
    private int userTotalScore;

    public void setUserHasCan(boolean userHasCan) {
        this.userHasCan = userHasCan;
    }

    public void setUserLeagueKind(int leagueKind){
        this.userLeagueKind = leagueKind;
    }
}
