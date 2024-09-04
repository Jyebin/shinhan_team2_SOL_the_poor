package com.choikang.poor.the_poor_back.repository;

import com.choikang.poor.the_poor_back.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u.userHasCan from User u where u.userID = :userID")
    Boolean findUserHasCanByUserID(Long userID);

    // 이메일을 통한 사용자 조회
    Optional<User> findByUserEmail(String userEmail);

    // 깡통 여부 업데이트
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.userHasCan = :hasCan WHERE u.userID = :userID")
    void updateUserHasCanById(@Param("userID") Long userID, @Param("hasCan") Boolean hasCan);

    // 작성 글 개수 업데이터
    @Modifying
    @Query("UPDATE User u SET u.userAttendanceCnt = :updatedAttendancePostCnt WHERE u.userID = :userID")
    void updateUserAttendanceCnt(@Param("userID") Long userID, @Param("updatedAttendancePostCnt") int updatedAttendancePostCnt);
}
