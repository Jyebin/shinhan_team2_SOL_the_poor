package com.choikang.poor.the_poor_back.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class User {
    @Id
    private int userID;

    private String userName;

    private String userPW;

    private String userEmail;

    private String userPhone;

    private int userAttendanceCnt;
}
