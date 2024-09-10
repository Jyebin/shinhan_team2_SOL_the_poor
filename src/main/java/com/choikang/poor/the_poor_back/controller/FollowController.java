package com.choikang.poor.the_poor_back.controller;

import com.choikang.poor.the_poor_back.dto.FollowDTO;
import com.choikang.poor.the_poor_back.service.FollowService;
import com.choikang.poor.the_poor_back.service.OAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost"})
@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;
    private final OAuth2UserService authService;

//    @GetMapping("/list/followers")
//    public ResponseEntity<?> getFollowersList() {
//
//    }

//    @GetMapping("/list/followings")
//    public ResponseEntity<?> getFollowingsList() {
//
//    }

    @PostMapping("/change")
    public ResponseEntity<?> changeFollowState(@RequestBody Map<String, Object> info, HttpServletRequest request) {
        String token = authService.getJWTFromCookies(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("토큰이 존재하지 않습니다.");
        }
        try {
            Long fromUserID = authService.getUserIDFromJWT(token);
            Long toUserID = Long.valueOf(info.get("toUserID").toString()); // id 가져오기
            FollowDTO followDTO = new FollowDTO(toUserID, fromUserID);
            followService.updateFollow(followDTO);
            return ResponseEntity.ok("HTTP 200");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }
}
