package com.choikang.poor.the_poor_back.account;

import com.choikang.poor.the_poor_back.model.Account;
import com.choikang.poor.the_poor_back.model.User;
import com.choikang.poor.the_poor_back.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AccountRepositoryTest {
    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void insertDummies() {
        User user = User.builder().userID((long)1).build();

        // 입출금 통장 1 -> 깡통 존재
        Account account1 = Account.builder()
                .accountBalance(100000000)
                .accountPW(1234)
                .accountName("쏠편한 입출금통장(저축예금)")
                .accountNumber("110-576-040419")
                .accountHasCan(true)
                .accountCanAmount(1700)
                .accountCanInterestRate(8.00)
                .user(user)
                .build();
        accountRepository.save(account1);

        // 입출금 통장 2 -> 깡통 없음
        Account account2 = Account.builder()
                .accountBalance(100000)
                .accountPW(1234)
                .accountName("신한 슈퍼SOL 통장")
                .accountNumber("110-233-298374")
                .accountHasCan(false)
                .user(user)
                .build();
        accountRepository.save(account2);
    }
}
