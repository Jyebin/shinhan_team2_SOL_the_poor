package com.choikang.poor.the_poor_back.rankingTest;

import com.choikang.poor.the_poor_back.model.Ranking;
import com.choikang.poor.the_poor_back.model.User;
import com.choikang.poor.the_poor_back.repository.RankingRepository;
import com.choikang.poor.the_poor_back.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RankingRepositoryTests {
    @Autowired
    private RankingRepository rankingRepository;

    @Test
    @DisplayName("랭킹 정보 더미 데이터 삽입")
    public void insertDummies() {
        for(int i = 0; i < 50; i++){
            Ranking ranking = Ranking.builder()
                    .rankingUserID((long) i)
                    .rankingUserName("사용자 이름" + i)
                    .rankingLeagueKind(i % 5 + 1)
                    .rankingScore(100*i + 100)
                    .userTotalScore(100*i)
                    .build();
            rankingRepository.save(ranking);
        }
    }


}
