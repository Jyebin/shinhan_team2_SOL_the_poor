package com.choikang.poor.the_poor_back.service;

import com.choikang.poor.the_poor_back.dto.UserDTO;
import com.choikang.poor.the_poor_back.model.User;
import com.choikang.poor.the_poor_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Boolean findUserHasCanByUserID(Long userID) {
        Optional<User> user = userRepository.findById(userID);
        return user.map(User::isUserHasCan).orElse(false);
    }

    public UserDTO getUserByID(Long userID) {
        Optional<User> user = userRepository.findById(userID);
        if (user.isPresent()) {
            User u = user.get();
            return new UserDTO(u.getUserID(), u.getUserName(), u.getUserEmail(), u.isUserHasCan());
        } else {
            throw new RuntimeException("User not found with ID: " + userID);
        }
    }

    // 사용자의 출석 횟수를 userID로 조회하는 메소드 추가
    public int findUserAttendanceCntByUserID(Long userID) {
        Optional<User> user = userRepository.findById(userID);
        if (user.isPresent()) {
            return user.get().getUserAttendanceCnt();
        } else {
            throw new IllegalArgumentException("유효하지 않은 사용자 ID입니다: " + userID);
        }
    }
}
