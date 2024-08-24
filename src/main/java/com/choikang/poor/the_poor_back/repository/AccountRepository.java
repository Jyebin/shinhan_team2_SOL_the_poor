package com.choikang.poor.the_poor_back.repository;

import com.choikang.poor.the_poor_back.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserID(Long userId);
}
