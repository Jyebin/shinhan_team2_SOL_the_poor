package com.choikang.poor.the_poor_back.controller;

import com.choikang.poor.the_poor_back.service.OAuth2UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@CrossOrigin(origins = {"http://localhost:3000/","http://localhost/"}) // 리액트에서 호출할 것이므로 리엑트의 url과 함께 작성
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final OAuth2UserService kakaoAuthService;

    @GetMapping("/oauth2/kakao")
    public ResponseEntity<String> kakaoCallback(@RequestParam("code") String code, HttpServletResponse response) throws Exception {
        String jwtToken = kakaoAuthService.kakaoLogin(code);
        System.out.println(jwtToken);

        // JWT를 쿠키에 저장
        Cookie cookie = new Cookie("token",  jwtToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        // JWT를 URL 파라미터로 전달하면서 리다이렉트
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("http://localhost:3000/login-success")
                .queryParam("token", jwtToken);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", uriBuilder.toUriString())
                .build();
    }


    @GetMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String accessToken = null;
        for(Cookie cookie : request.getCookies()){
            if(cookie.getName().equals("token")){
                accessToken = cookie.getValue();
            }
        }
        System.out.println("이부분 실행 되나/?");
        System.out.println(accessToken);

        if(accessToken != null){
            kakaoAuthService.kakaoLogout(accessToken);

            // 쿠키 삭제
            Cookie cookie = new Cookie("token", null);
            cookie.setPath(request.getContextPath());  // request에서 ContextPath 가져오기
            cookie.setHttpOnly(true);
            cookie.setMaxAge(0);  // 즉시 만료
            cookie.setValue(null);  // 명시적으로 값 null 설정
            response.addCookie(cookie);

        }
        return ResponseEntity.ok().build();
    }


}
