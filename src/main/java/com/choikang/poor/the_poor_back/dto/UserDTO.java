package com.choikang.poor.the_poor_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long userID;
    private String userName;
    private String userEmail;
    private Boolean userHasCan;
    private int userAttendanceCnt;
    private String userCode;
    private int userFollower;
    private int userFollowing;
}
