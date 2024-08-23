package com.choikang.poor.the_poor_back.repository;

import com.choikang.poor.the_poor_back.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT u.userHasCan FROM User u WHERE u.userID = :userID")
    Boolean findUserHasCanByUserID(@Param("userID") Long userID);
}
