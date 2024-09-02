package com.choikang.poor.the_poor_back.controller;

import com.choikang.poor.the_poor_back.dto.AttendancePostResponseDTO;
import com.choikang.poor.the_poor_back.dto.AttendancePostsRequestDTO;
import com.choikang.poor.the_poor_back.service.AttendancePostsService;
import com.choikang.poor.the_poor_back.service.OAuth2UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/attendance")
public class AttendancePostsController {
    @Autowired
    private AttendancePostsService attendancePostsService;

    @Autowired
    private OAuth2UserService oAuth2UserService;

    @PostMapping("/create")
    public ResponseEntity<String[]> createPost(HttpServletRequest request,
                                               @RequestBody AttendancePostsRequestDTO attendancePostsRequestDTO)
            throws Exception {
        String token = null;
        System.out.println("ㅇㅕ기야 여기 : " + request);
        System.out.println("또 여기야 여기 : " + attendancePostsRequestDTO);
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
            attendancePostsRequestDTO.setUserId(userId);
            String[] responseContent = attendancePostsService.createPost(attendancePostsRequestDTO);
            System.out.println("responseContent : " + responseContent);
            return new ResponseEntity<>(responseContent, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/view")
    public ResponseEntity<?> viewAttendance(HttpServletRequest request){
        try {
            String token = oAuth2UserService.getJWTFromCookies(request);
            Long userID = oAuth2UserService.getUserID(token);
            Optional<List<AttendancePostResponseDTO>> userPostList = attendancePostsService.getAttendancePostList(userID);

            if(userPostList.isPresent()){
                return new ResponseEntity<>(userPostList.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}