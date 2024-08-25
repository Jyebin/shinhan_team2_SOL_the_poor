package com.choikang.poor.the_poor_back.controller;

import com.choikang.poor.the_poor_back.service.OAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;


@CrossOrigin(origins = {"http://localhost:3000/","http://localhost/"}) // 리액트에서 호출할 것이므로 리엑트의 url과 함께 작성
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final OAuth2UserService kakaoAuthService;

    // 로그인 하기 누를 시 실행.
    @GetMapping("/oauth2/kakao")
    public ResponseEntity<String> kakaoCallback(@RequestParam("code") String code) throws Exception {
        String jwtToken = kakaoAuthService.kakaoLogin(code);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("http://localhost:3000")
                .queryParam("token", jwtToken);
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", uriBuilder.toUriString()).build();
    }

    @GetMapping("/logout")
    public void logout(){

    }

}
