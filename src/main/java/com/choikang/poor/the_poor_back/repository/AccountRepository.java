package com.choikang.poor.the_poor_back.repository;

import com.choikang.poor.the_poor_back.dto.AccountDTO;
import com.choikang.poor.the_poor_back.dto.TransactionDTO;
import com.choikang.poor.the_poor_back.model.Account;
import com.choikang.poor.the_poor_back.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserUserID(Long userID);
    @Query("SELECT a.accountCanAmount FROM Account a WHERE a.accountID = :accountID")
    int findCanAmountByAccountID (@Param("accountID") Long accountID);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.userHasCan = true WHERE u.userID = :userID")
    void updateUserHasCan(@Param("userID") Long userID);

    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.accountHasCan = true WHERE a.accountID = :accountID")
    void updateAccountHasCan(@Param("accountID") Long accountID);
}
