package com.choikang.poor.the_poor_back.service;

import com.choikang.poor.the_poor_back.dto.AttendancePostResponseDTO;
import com.choikang.poor.the_poor_back.dto.AttendancePostsRequestDTO;
import com.choikang.poor.the_poor_back.dto.OpenAIRequestDTO;
import com.choikang.poor.the_poor_back.model.AttendancePosts;
import com.choikang.poor.the_poor_back.model.User;
import com.choikang.poor.the_poor_back.repository.AttendancePostsRepository;
import com.choikang.poor.the_poor_back.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        if(!responseType.equals("'판단안됨'")){
            AttendancePosts attendancePosts = AttendancePosts.builder()
                    .user(user)
                    .attendanceDate(LocalDateTime.now())
                    .attendanceType(attendanceType)
                    .attendanceContent(postsDTO.getMessage())
                    .build();
            attendancePostsRepository.save(attendancePosts);
        }

        String[] responses = new String[2];
        responses[0] = responseType;
        responses[1] = responseContent;
        return responses;
    }

    private OpenAIRequestDTO createOpenAIRequest(String content) {
        OpenAIRequestDTO.Message systemMessage = new OpenAIRequestDTO.Message(
                "system",
                "You are a helpful assistant that categorizes text into three categories: Reflection ('과소비'), Frugality Confirmation ('절약'), and Neither ('판단안됨'). Based on the given text, you will return a response in the format [category, content]. The content should be a message you would give to a friend: scolding for '과소비' or praise for '절약인증'. Use emojis to make it feel like a friendly conversation, and keep the message under 200 characters. Please write it without using a comma in the content. Don't use honorifics. Pretend you're talking informally to your friend. Focus on your current spending. Don't argue about cost-effectiveness or efficiency."
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
            case "'과소비'":
                answer = 1;
                break;
            case "'절약'":
                answer = 2;
                break;
            case "'판단안됨'":
                answer = 3;
                break;
        }
        return answer;
    }

    public Optional<List<AttendancePostResponseDTO>> getAttendancePostList (Long userID){
        List<AttendancePostResponseDTO> posts = attendancePostsRepository.findByUserUserID(userID)
                .stream()
                .map(post -> {
                    AttendancePostResponseDTO dto = new AttendancePostResponseDTO();
                    dto.setDate(post.getAttendanceDate().toString().substring(0, 10));
                    dto.setContent(post.getAttendanceContent());
                    dto.setType(switchPostTypeFromNumToWord(post.getAttendanceType()));
                    return dto;
                })
                .collect(Collectors.toList());

        return Optional.ofNullable(posts);
    }

    public String switchPostTypeFromNumToWord(int typeNum){
        if(typeNum == 1){
            return "overspending";
        }
        return "saving";
    }
}