package com.choikang.poor.the_poor_back.service;

import com.choikang.poor.the_poor_back.dto.RankingDTO;
import com.choikang.poor.the_poor_back.dto.RankingResponseDTO;
import com.choikang.poor.the_poor_back.model.Ranking;
import com.choikang.poor.the_poor_back.repository.RankingRepository;
import com.choikang.poor.the_poor_back.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class RankingService {
    private final OAuth2UserService oAuth2UserService;
    private final RankingRepository rankingRepository;
    private final UserService userService;

    public ResponseEntity<?> getAllUserRanking(HttpServletRequest request) {
        try {
            // 사용자가 속한 리그의 정보 가져오기
            long userID = oAuth2UserService.getUserIDFromJWT(oAuth2UserService.getJWTFromCookies(request));
            List<RankingDTO> rankingList = getRankingDTOs(rankingRepository.findByRankingLeagueKindOrderByRankingScoreDesc(userID));

            if (rankingList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            String leagueKind = getUserLeagueKind(userID);
            if (leagueKind == null) {
                return new ResponseEntity<>("Invalid league kind", HttpStatus.BAD_REQUEST);
            }

            RankingResponseDTO rankingResponseDTO = RankingResponseDTO.builder()
                    .rankingDTOList(rankingList)
                    .leagueKind(leagueKind)
                    .build();

            return new ResponseEntity<>(rankingResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getUserLeagueKind(long userID) {
        int leagueKindValue = userService.getUserByID(userID).getUserLeagueKind();
        switch (leagueKindValue) {
            case 1: return "bronze";
            case 2: return "silver";
            case 3: return "gold";
            case 4: return "platinum";
            case 5: return "dia";
            default: return null;
        }
    }


    public List<RankingDTO> getRankingDTOs(List<Ranking> rankings) {
        if (rankings == null || rankings.isEmpty()) {
            return Collections.emptyList();
        }

        return IntStream.range(0, rankings.size())
                .mapToObj(index -> {
                    Ranking currentRanking = rankings.get(index);
                    return rankingDTO(currentRanking, index + 1);
                })
                .collect(Collectors.toList());
    }

    private RankingDTO rankingDTO(Ranking ranking, int rankingNum) {
        return RankingDTO.builder()
                .rankingNum(rankingNum)
                .rankingUserName(ranking.getRankingUserName())
                .rankingScore(ranking.getRankingScore())
                .userTotalScore(ranking.getUserTotalScore())
                .build();
    }
}
