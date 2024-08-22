package com.choikang.poor.the_poor_back.user;

import com.choikang.poor.the_poor_back.model.User;
import com.choikang.poor.the_poor_back.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryTest {
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
}
