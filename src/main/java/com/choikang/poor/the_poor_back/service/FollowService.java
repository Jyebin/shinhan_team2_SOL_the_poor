package com.choikang.poor.the_poor_back.service;

import com.choikang.poor.the_poor_back.dto.FollowDTO;
import com.choikang.poor.the_poor_back.dto.FollowResponseDTO;
import com.choikang.poor.the_poor_back.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;

//    public List<FollowResponseDTO> getFollowingList() {
//
//    }

    public void updateFollow(FollowDTO followDTO) {

    }
}
