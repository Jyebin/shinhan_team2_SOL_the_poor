package com.choikang.poor.the_poor_back.rankingTest;

import com.choikang.poor.the_poor_back.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RankingServiceTests {
    @Autowired
    UserService userService;

    @Test
    @DisplayName("랭킹 정보 호출 테스트")
    public void findUserHasCanByUserID() {
        Long userID = (long) 1;
        boolean result = userService.findUserHasCanByUserID(userID);
        Assertions.assertEquals(result, true);
    }
}
