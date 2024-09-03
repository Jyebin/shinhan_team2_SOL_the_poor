package com.choikang.poor.the_poor_back.accountTest;

import com.choikang.poor.the_poor_back.model.Account;
import com.choikang.poor.the_poor_back.service.banking.AccountService;
import com.choikang.poor.the_poor_back.service.banking.CanService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CanServiceTests {
    @Autowired
    CanService canService;

    @Test
    @DisplayName("깡통 잔액 테스트")
    public void getCanAmountTest() {
        int amount = 1700;
        int testAmount = canService.getCanAmountByAccountID(1L);
        Assertions.assertEquals(amount, testAmount);
    }

    @Test
    @DisplayName("깡통 해지 테스트")
    public void canTerminationTest() {

    }

    @Test
    @DisplayName("깡통 가입 테스트")
    public void canRegisterTest() {

    }

}
