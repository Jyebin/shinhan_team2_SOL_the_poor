package com.choikang.poor.the_poor_back.service;

import com.choikang.poor.the_poor_back.dto.RankingDTO;
import com.choikang.poor.the_poor_back.dto.RankingResponseDTO;
import com.choikang.poor.the_poor_back.model.Ranking;
import com.choikang.poor.the_poor_back.repository.RankingRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
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
            int leagueKindValue = userService.getUserByID(userID).getUserLeagueKind();

            List<RankingDTO> rankingList = getRankingDTOs(rankingRepository.findByRankingLeagueKindOrderByRankingMonthScoreDesc(leagueKindValue));

            if (rankingList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            String leagueKind = getUserLeagueKind(userID);
            if (leagueKind == null) {
                return new ResponseEntity<>("Invalid league kind", HttpStatus.BAD_REQUEST);
            }

            RankingResponseDTO rankingResponseDTO = RankingResponseDTO.builder()
                    .rankingDTOList(rankingList)
                    .leagueKind(leagueKind)
                    .rankingUserID((int)userID)
                    .build();

            return new ResponseEntity<>(rankingResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?>  getRankingDataByLeague(String leagueKind) {
        // 리그 이름에 맞는 데이터를 DB에서 가져옴
        int leagueKindCode = convertLeagueKindToCode(leagueKind);
        List<RankingDTO> rankingList = getRankingDTOs(rankingRepository.findByRankingLeagueKindOrderByRankingMonthScoreDesc(leagueKindCode));
        if (rankingList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        RankingResponseDTO rankingResponseDTO = RankingResponseDTO.builder()
                .rankingDTOList(rankingList)
                .leagueKind(leagueKind)
                .build();

        return new ResponseEntity<>(rankingResponseDTO, HttpStatus.OK);
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

    private int convertLeagueKindToCode(String leagueKind) {
        switch (leagueKind.toLowerCase()) {
            case "bronze": return 1;
            case "silver": return 2;
            case "gold": return 3;
            case "platinum": return 4;
            case "dia": return 5;
            default: throw new IllegalArgumentException("Invalid league kind");
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
                .rankingUserNum(ranking.getRankingUserID())
                .rankingUserName(ranking.getRankingUserName())
                .rankingScore(ranking.getRankingMonthScore())
                .userTotalScore(ranking.getRankingUserTotalScore())
                .build();
    }
}
