package com.choikang.poor.the_poor_back.userTest;

import com.choikang.poor.the_poor_back.model.User;
import com.choikang.poor.the_poor_back.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void insertDummies() {
        User user = User.builder()
                .userEmail("abc@gmail.com")
                .userName("abc")
                .userAttendanceCnt(0)
                .userHasCan(true)
                .build();
        userRepository.save(user);
    }

    @Test
    @DisplayName("유저가 깡통을 가지고 있는지 확인하는 테스트")
    public void checkUserHasCan() {
        Long userID = 1L;
        Boolean result = userRepository.findUserHasCanByUserID(userID);
        Assertions.assertEquals(result, true);
    }
}
