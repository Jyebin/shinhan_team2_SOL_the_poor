package com.choikang.poor.the_poor_back.controller;

import com.choikang.poor.the_poor_back.dto.UserDTO;
import com.choikang.poor.the_poor_back.service.OAuth2UserService;
import com.choikang.poor.the_poor_back.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000", "http://localhost"})
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    OAuth2UserService authService;

    // 유저의 깡통 여부 찾아서 반환
    @GetMapping("/hasCan")
    public ResponseEntity<?> getAccountHasCan(HttpServletRequest request) {
        String token = authService.getJWTFromCookies(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("토큰이 존재하지 않습니다.");
        }
        try {
            Long userID = authService.getUserIDFromJWT(token);
            Boolean userHasCan = userService.findUserHasCanByUserID(userID);
            return ResponseEntity.ok(userHasCan);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }

    // 현재 로그인된 유저의 정보를 반환하는 엔드포인트
    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        String token = authService.getJWTFromCookies(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("토큰이 존재하지 않습니다.");
        }
        try {
            Long userID = authService.getUserIDFromJWT(token);
            UserDTO userDTO = userService.getUserByID(userID);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }

    @GetMapping("/info/other")
    public ResponseEntity<?> getOtherUserInfo(@RequestParam("userID") Long userID) {
        try {
            UserDTO userDTO = userService.getUserByID(userID);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }

    @GetMapping("/search/{userCode}")
    public ResponseEntity<?> findUserByUserCode(@PathVariable String userCode) {
        try {
            return ResponseEntity.ok(userService.findUserByUserCode(userCode));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }
}
