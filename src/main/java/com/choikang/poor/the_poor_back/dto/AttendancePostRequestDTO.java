package com.choikang.poor.the_poor_back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendancePostRequestDTO {
    private Long userId;
    private String content;
    private Date date;
}
