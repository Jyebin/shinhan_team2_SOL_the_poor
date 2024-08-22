package com.choikang.poor.the_poor_back.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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
}
