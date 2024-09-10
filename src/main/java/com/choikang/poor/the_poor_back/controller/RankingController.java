package com.choikang.poor.the_poor_back.controller;

import com.choikang.poor.the_poor_back.service.RankingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ranking")
public class RankingController {

    private final RankingService rankingService;

    @GetMapping("")
    public ResponseEntity<?> viewRankingData(HttpServletRequest request) {
        try {
            return rankingService.getAllUserRanking(request);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/league/data")
    public ResponseEntity<?> viewRankingDataFromLeagueKind(@RequestBody Map<String, Object> requestData) {
        try {
            String league = (String) requestData.get("league");

            return rankingService.getRankingDataByLeague(league);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
