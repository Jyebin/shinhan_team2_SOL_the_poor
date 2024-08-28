package com.choikang.poor.the_poor_back.controller;

import com.choikang.poor.the_poor_back.dto.AttendancePostResponseDTO;
import com.choikang.poor.the_poor_back.dto.AttendancePostsDTO;
import com.choikang.poor.the_poor_back.service.AttendancePostsService;
import com.choikang.poor.the_poor_back.service.OAuth2UserService;
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
    public ResponseEntity<String> createPost(@RequestBody AttendancePostsDTO attendancePostsDTO) {
        try{
            String responseContent = attendancePostsService.createPost(attendancePostsDTO);
            return new ResponseEntity<>(responseContent, HttpStatus.CREATED);
        }catch (RuntimeException e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/viewAllAttendance")
    public ResponseEntity<?> viewAttendance(HttpServletRequest request){
        try {
            String token = oAuth2UserService.getJWTFromCookies(request);
            System.out.println(token);
            Long userID = oAuth2UserService.getUserID(token);
            System.out.println(oAuth2UserService.getUserAccessToken(token));
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
