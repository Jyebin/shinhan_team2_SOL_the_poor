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
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    OAuth2UserService authService;

    @GetMapping("/hasCan")
    public ResponseEntity<?> getAccountHasCan(HttpServletRequest request) {
        String token = null;
        for(Cookie cookie : request.getCookies()){ // 쿠키에서 토큰 값 찾기
            if(cookie.getName().equals("token")){
                token = cookie.getValue();
            }
        }
        if (token == null) { // 토큰 값이 없으면
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("토큰이 존재하지 않습니다.");
        }

        try { // 토큰을 통해 userID, hasCan 값 찾기
            Long userID = authService.getUserID(token);
            Boolean userHasCan = userService.findUserHasCanByUserID(userID);
            return ResponseEntity.ok(userHasCan);
        } catch(Exception e) {
            return ResponseEntity // 실패 시
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal Server Error");
        }
    }
}
