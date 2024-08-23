package com.choikang.poor.the_poor_back.accountTest;

import com.choikang.poor.the_poor_back.model.Account;
import com.choikang.poor.the_poor_back.model.User;
import com.choikang.poor.the_poor_back.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AccountServiceTests {
    @Autowired
    AccountService accountService;

    @Test
    @DisplayName("깡통 여부 반환 테스트")
    public void heHasCanTest() {
        Long userID = (long) 1;
        Boolean userHasCan = accountService.findUserHasCan(userID);
        Assertions.assertEquals(userHasCan, true);
    }
}
