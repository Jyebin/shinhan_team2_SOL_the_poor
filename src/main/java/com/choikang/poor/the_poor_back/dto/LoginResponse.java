package com.choikang.poor.the_poor_back.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@Log4j2
@Builder
public class LoginResponse {
    private String message;
    private String token;
}
