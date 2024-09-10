package com.choikang.poor.the_poor_back.repository;

import com.choikang.poor.the_poor_back.model.AttendancePosts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendancePostsRepository extends JpaRepository<AttendancePosts, Integer> {
    // 유저 아이디를 통해 작성 글 전체 목록 조회
    List<AttendancePosts> findByUserUserID(Long userID);

    // 유저 아이디를 통해 작성 글 개수 조회
    @Query("SELECT COUNT(p) FROM AttendancePosts p WHERE p.user.userID = :userID")
    int countAllByUserUserID(@Param("userID") Long userID);
}
