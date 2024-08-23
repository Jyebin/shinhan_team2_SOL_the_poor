package com.choikang.poor.the_poor_back.dto;

import lombok.Data;

@Data
public class KakaoLoginDTO {
    private String accessToken;
    private String tokenType;
    private String reFreshToken;
    private String idToken;
    private int expiresIn;
    private int reFreshExpiresIn;
    private String scope;
}
