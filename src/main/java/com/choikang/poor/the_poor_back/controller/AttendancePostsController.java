package com.choikang.poor.the_poor_back.controller;

import com.choikang.poor.the_poor_back.dto.AttendancePostsDTO;
import com.choikang.poor.the_poor_back.service.AttendancePostsService;
import com.choikang.poor.the_poor_back.service.OAuth2UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attendance")
public class AttendancePostsController {
    @Autowired
    private AttendancePostsService attendancePostsService;

    @Autowired
    private OAuth2UserService oAuth2UserService;

    @PostMapping("/create")
    public ResponseEntity<String[]> createPost(HttpServletRequest request,
                                               @RequestBody AttendancePostsDTO attendancePostsDTO)
            throws Exception {
        String token = null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("token")) {
                token = cookie.getValue();
            }
        }
        if (token == null) {
            return new ResponseEntity<>(new String[0], HttpStatus.UNAUTHORIZED);
        }
        try {
            Long userId = oAuth2UserService.getUserID(token);
            attendancePostsDTO.setUserId(userId);
            String[] responseContent = attendancePostsService.createPost(attendancePostsDTO);
            return new ResponseEntity<>(responseContent, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
