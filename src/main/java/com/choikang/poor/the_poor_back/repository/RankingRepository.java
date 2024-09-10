package com.choikang.poor.the_poor_back.repository;

import com.choikang.poor.the_poor_back.model.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RankingRepository extends JpaRepository<Ranking, Long> {
    List<Ranking> findByRankingLeagueKindOrderByRankingMonthScoreDesc(int leagueKind);
    Ranking findRankingByRankingUserID(long userID);
}
