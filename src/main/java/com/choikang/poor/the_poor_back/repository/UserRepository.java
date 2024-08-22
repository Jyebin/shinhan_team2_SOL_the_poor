package com.choikang.poor.the_poor_back.repository;

import com.choikang.poor.the_poor_back.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
