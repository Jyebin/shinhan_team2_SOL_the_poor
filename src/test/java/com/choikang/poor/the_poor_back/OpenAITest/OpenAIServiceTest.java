package com.choikang.poor.the_poor_back.OpenAITest;

import com.choikang.poor.the_poor_back.dto.OpenAIRequestDTO;
import com.choikang.poor.the_poor_back.service.OpenAIService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class OpenAIServiceTest {

    @Autowired
    private OpenAIService openAIService;

    @Test
    void testOpenAIRequest() {
        // 더미데이터 넣기
        OpenAIRequestDTO requestDTO = new OpenAIRequestDTO();
        requestDTO.setModel("gpt-4");
        requestDTO.setMessages(List.of(
                new OpenAIRequestDTO.Message("system",
                        "You are a helpful assistant that categorizes text into three categories: Reflection ('반성문'), Frugality Confirmation ('절약 인증'), and Neither ('판단 안됨'). Based on the given text, you will return a response in the format category, content. The content should be a message you would give to a friend: scolding for '반성문' or praise for '절약 인증'. Use emojis to make it feel like a friendly conversation, and keep the message under 200 characters. Please write it without using a comma in the content. Don't use honorifics. Pretend you're talking informally to your friend. Focus on your current spending. Don't argue about cost-effectiveness or efficiency."),
                new OpenAIRequestDTO.Message("user", "당근마켓에서 100만원주고 노트북 샀어")
        ));
        requestDTO.setTemperature(0.7);
        requestDTO.setMax_tokens(200);

        //응답 받아와서 추출 및 출력하기
        try {
            String[] response = openAIService.getResponseMessage(requestDTO);
            System.out.println(response);
            String gptAnswerType = response[0];
            String gptAnswerContent = response[1];

            System.out.println("타입: " + gptAnswerType);
            System.out.println("내용: " + gptAnswerContent);
        } catch (Exception e) {
            System.out.println("예외 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
