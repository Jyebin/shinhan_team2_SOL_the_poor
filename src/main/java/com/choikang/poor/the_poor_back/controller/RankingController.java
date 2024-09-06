package com.choikang.poor.the_poor_back.controller;

import com.choikang.poor.the_poor_back.service.RankingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ranking")
public class RankingController {

    private final RankingService rankingService;

    @GetMapping("")
    public ResponseEntity<?> viewAttendance(HttpServletRequest request) {
        try {
            return rankingService.getAllUserRanking(request);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
