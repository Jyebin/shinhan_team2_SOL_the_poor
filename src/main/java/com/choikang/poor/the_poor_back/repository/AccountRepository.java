package com.choikang.poor.the_poor_back.repository;

import com.choikang.poor.the_poor_back.dto.AccountDTO;
import com.choikang.poor.the_poor_back.dto.TransactionDTO;
import com.choikang.poor.the_poor_back.model.Account;
import com.choikang.poor.the_poor_back.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserUserID(Long userID);
    @Query("SELECT a.accountCanAmount FROM Account a WHERE a.accountID = :accountID")
    int findCanAmountByAccountID (@Param("accountID") Long accountID);
    Optional<Account> findByAccountNumber(String accountNumber);
}
