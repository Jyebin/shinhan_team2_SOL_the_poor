package com.choikang.poor.the_poor_back.service;

import com.choikang.poor.the_poor_back.dto.UserDTO;
import com.choikang.poor.the_poor_back.model.User;
import com.choikang.poor.the_poor_back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Boolean findUserHasCanByUserID(Long userID) {
        return userRepository.findUserHasCanByUserID(userID);
    }

    public UserDTO getUserByID(Long userID) {
        Optional<User> user = userRepository.findById(userID);
        if (user.isPresent()) {
            User u = user.get();
            return new UserDTO(u.getUserID(), u.getUserName(), u.getUserEmail(), u.isUserHasCan(), u.getUserAttendanceCnt(), u.getUserCode(), u.getUserFollower(), u.getUserFollower(),u.getUserLeagueKind());
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

    // userCode로 user 목록 조회
    public List<UserDTO> findUserByUserCode(String userCode) {
        List<User> userList = userRepository.findByUserCode(userCode);
        List<UserDTO> userDTOs = userList.stream()
                .map(user ->  UserDTO.builder()
                        .userID(user.getUserID())
                        .userName(user.getUserName())
                        .userCode(user.getUserCode())
                        .build()
                )
                .collect(Collectors.toList());
        return userDTOs;
    }
}
