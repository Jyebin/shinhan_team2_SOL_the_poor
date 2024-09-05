package com.choikang.poor.the_poor_back.controller;

import com.choikang.poor.the_poor_back.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost"})
@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

//    @GetMapping("/list/followers")
//    public ResponseEntity<?> getFollowersList() {
//
//    }

//    @GetMapping("/list/followings")
//    public ResponseEntity<?> getFollowingsList() {
//
//    }
}
