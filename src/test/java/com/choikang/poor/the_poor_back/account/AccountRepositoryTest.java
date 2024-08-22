package com.choikang.poor.the_poor_back.account;

import com.choikang.poor.the_poor_back.model.Account;
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
        Account account = Account.builder()
                .accountBalance(100000000)
                .accountPW(1234)
                .accountName("쏠편한 입출금통장(저축예금)")
                .accountNumber("110-576-040419")
                .accountHasCan(true)
                .accountCanAmount(13000)
                .accountCanInterestRate(8.00)
                .build();
        accountRepository.save(account);
    }
}
