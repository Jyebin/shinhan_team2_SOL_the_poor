package com.choikang.poor.the_poor_back.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@CrossOrigin(origins = {"http://localhost:3000/","http://localhost/"}) // 리액트에서 호출할 것이므로 리엑트의 url과 함께 작성
@RestController
public class AuthController {

    @GetMapping("/oauth2/kakao")
    public void kakaoCallback(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        System.out.println("Received Kakao auth code: " + code);
        // 여기서 카카오 API를 호출하여 액세스 토큰과 사용자 정보를 얻습니다.
        // 실제 구현에서는 이 정보를 처리하고 자체 토큰을 생성해야 합니다.

        // 처리가 완료되면 리액트 앱으로 리다이렉트합니다.
        response.sendRedirect("http://localhost:3000/login-success?token=sample_token");
    }

}
