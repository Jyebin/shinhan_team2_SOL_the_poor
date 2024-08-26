package com.choikang.poor.the_poor_back.controller;

import com.choikang.poor.the_poor_back.dto.AttendancePostsDTO;
import com.choikang.poor.the_poor_back.model.AttendancePosts;
import com.choikang.poor.the_poor_back.service.AttendancePostsService;
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

    @PostMapping("/create")
    public ResponseEntity<String> createPost(@RequestBody AttendancePostsDTO attendancePostsDTO) {
        try{
            String responseContent = attendancePostsService.createPost(attendancePostsDTO);
            return new ResponseEntity<>(responseContent, HttpStatus.CREATED);
        }catch (RuntimeException e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
