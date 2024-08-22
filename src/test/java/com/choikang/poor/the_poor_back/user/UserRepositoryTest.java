package com.choikang.poor.the_poor_back.user;

import com.choikang.poor.the_poor_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository repository;
}
