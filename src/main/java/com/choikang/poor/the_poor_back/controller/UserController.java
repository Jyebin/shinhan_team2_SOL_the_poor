package com.choikang.poor.the_poor_back.controller;

import com.choikang.poor.the_poor_back.service.OAuth2UserService;
import com.choikang.poor.the_poor_back.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000/","http://localhost/"})
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    OAuth2UserService authService;

    // 유저의 깡통 여부 찾아서 앞단으로 반환
    @GetMapping("/hasCan")
    public ResponseEntity<?> getAccountHasCan(HttpServletRequest request) {
        String token = authService.getJWTFromCookies(request);
        if (token == null) // 토큰 값이 없으면 BAD_REQUEST 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("토큰이 존재하지 않습니다.");
        try { // userID, hasCan 값 찾는 Service 호출
            Long userID = authService.getUserID(token);
            Boolean userHasCan = userService.findUserHasCanByUserID(userID);
            return ResponseEntity.ok(userHasCan);
        } catch(Exception e) { // 실패 시 INTERNAL SERVER ERROR 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }
}
