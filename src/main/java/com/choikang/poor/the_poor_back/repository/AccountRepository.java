package com.choikang.poor.the_poor_back.repository;

import com.choikang.poor.the_poor_back.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserUserID(Long userID);

    @Query("SELECT a.accountCanAmount FROM Account a WHERE a.accountID = :accountID")
    int findCanAmountByAccountID (@Param("accountID") Long accountID);

    @Query("SELECT a.accountBalance FROM Account a WHERE a.accountID = :accountID")
    int findAmountByAccountID (@Param("accountID") Long accountID);

    @Query("SELECT a.user.userID FROM Account a WHERE a.accountID = :accountID")
    Long findUserIDByAccountID(@Param("accountID") Long accountID);

    // hascan 업데이트
    @Modifying
    @Query("UPDATE Account a SET a.accountHasCan = :accountHasCan WHERE a.accountID = :accountID")
    @Transactional
    void updateAccountHasCanByAccountID(@Param("accountID") Long accountID, @Param("accountHasCan") Boolean accountHasCan);

    // 깡통 해지 시 잔액 조정
    @Modifying
    @Query("UPDATE Account a" +
            " SET a.accountBalance = a.accountBalance + a.accountCanAmount," +
            " a.accountCanAmount = 0" +
            " WHERE a.accountID = :accountID")
    @Transactional
    void updateBalanceAndResetCanAmount(@Param("accountID") Long accountID);

    Optional<Account> findByAccountNumber(String accountNumber);

}
