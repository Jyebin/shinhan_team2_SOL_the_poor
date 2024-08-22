package com.choikang.poor.the_poor_back.account;

import com.choikang.poor.the_poor_back.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AccountRepositoryTest {
    @Autowired
    private AccountRepository repository;

    @Test
    public void insertDummies() {

    }
}
