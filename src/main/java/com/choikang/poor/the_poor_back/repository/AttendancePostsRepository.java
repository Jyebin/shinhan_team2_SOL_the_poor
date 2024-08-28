package com.choikang.poor.the_poor_back.repository;

import com.choikang.poor.the_poor_back.model.AttendancePosts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendancePostsRepository extends JpaRepository<AttendancePosts, Integer> {
    // 유저 아이디를 통해 작성 글 전체 목록 조회
    List<AttendancePosts> findByUserID(Long userID);
}
