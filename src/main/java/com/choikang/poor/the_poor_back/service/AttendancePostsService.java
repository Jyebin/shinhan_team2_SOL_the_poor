package com.choikang.poor.the_poor_back.service;

import com.choikang.poor.the_poor_back.dto.AttendancePostsRequestDTO;
import com.choikang.poor.the_poor_back.dto.OpenAIRequestDTO;
import com.choikang.poor.the_poor_back.model.AttendancePosts;
import com.choikang.poor.the_poor_back.model.User;
import com.choikang.poor.the_poor_back.repository.AttendancePostsRepository;
import com.choikang.poor.the_poor_back.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendancePostsService {
    @Autowired
    private AttendancePostsRepository attendancePostsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OpenAIService openAIService;

    public String[] createPost(AttendancePostsRequestDTO postsDTO) {

        User user = userRepository.findById(postsDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        OpenAIRequestDTO openAIRequestDTO = createOpenAIRequest(postsDTO.getMessage());

        String[] responseArr = openAIService.getResponseMessage(openAIRequestDTO);
        String responseType = responseArr[0];
        int attendanceType = determineAttendanceType(responseArr[0]);
        String responseContent = responseArr[1];

        AttendancePosts attendancePosts = AttendancePosts.builder()
                .user(user)
                .attendanceDate(LocalDateTime.now())
                .attendanceType(attendanceType)
                .attendanceContent(postsDTO.getMessage())
                .build();
        attendancePostsRepository.save(attendancePosts);
        String[] responses = new String[2];
        responses[0] = responseType;
        responses[1] = responseContent;
        return responses;
    }

    private OpenAIRequestDTO createOpenAIRequest(String content) {
        OpenAIRequestDTO.Message systemMessage = new OpenAIRequestDTO.Message(
                "system",
                "You are a helpful assistant that categorizes text into three categories: Reflection ('반성문'), Frugality Confirmation ('절약 인증'), and Neither ('판단 안됨'). Based on the given text, you will return a response in the format [category, content]. The content should be a message you would give to a friend: scolding for '반성문' or praise for '절약 인증'. Use emojis to make it feel like a friendly conversation, and keep the message under 200 characters. Please write it without using a comma in the content. Don't use honorifics. Pretend you're talking informally to your friend. Focus on your current spending. Don't argue about cost-effectiveness or efficiency."
        );

        OpenAIRequestDTO.Message userMessage = new OpenAIRequestDTO.Message(
                "user",
                content
        );

        return new OpenAIRequestDTO(
                "gpt-4",
                Arrays.asList(systemMessage, userMessage),
                0.7,
                200
        );
    }

    private int determineAttendanceType(String attendanceType) {
        int answer = 0;
        switch (attendanceType) {
            case "'반성문'":
                answer = 1;
                break;
            case "'절약 인증'":
                answer = 2;
                break;
            case "'판단 안됨'":
                answer = 3;
                break;
        }
        return answer;
    }
}
