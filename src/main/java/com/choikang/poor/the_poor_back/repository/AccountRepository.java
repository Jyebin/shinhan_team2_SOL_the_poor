package com.choikang.poor.the_poor_back.repository;

import com.choikang.poor.the_poor_back.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
