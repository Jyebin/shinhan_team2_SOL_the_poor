package com.choikang.poor.the_poor_back.service;

import com.choikang.poor.the_poor_back.model.User;
import com.choikang.poor.the_poor_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public Boolean findUserHasCanByUserID(Long userID) {
        Optional<User> result = userRepository.findById(userID);
        User user = result.get();
        return user.isUserHasCan();
    }

    public int findUserAttendanceCtnByUserID(Long userID) {
        Optional<User> result = userRepository.findById(userID);
        User user = result.get();
        return user.getUserAttendanceCnt();
    }
}
