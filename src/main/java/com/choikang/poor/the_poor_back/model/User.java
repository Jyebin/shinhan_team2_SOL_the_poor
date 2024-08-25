package com.choikang.poor.the_poor_back.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;

    private String userName;
    private String userEmail;
    private int userAttendanceCnt;

    // 출석 횟수 증가 메서드
    public void incrementAttendance() {
        this.userAttendanceCnt++;
    }
}
