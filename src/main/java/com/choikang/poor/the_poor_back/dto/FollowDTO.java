package com.choikang.poor.the_poor_back.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FollowDTO {
    private Long toUserID;
    private Long fromUserID;
}
