package com.choikang.poor.the_poor_back.repository;

import com.choikang.poor.the_poor_back.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일을 통한 사용자 조회
    Optional<User> findByUserEmail(String userEmail);
}
