package com.choikang.poor.the_poor_back.service;

import com.choikang.poor.the_poor_back.dto.OpenAIRequestDTO;
import com.choikang.poor.the_poor_back.dto.OpenAIResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAIService {
    private final RestTemplate restTemplate;

    @Autowired
    public OpenAIService(@Qualifier("openAIRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    //응답값에서 메세지만 추출
    public String[] getResponseMessage(OpenAIRequestDTO requestDTO) {
        // 요청 본문 설정
        HttpEntity<OpenAIRequestDTO> requestEntity = new HttpEntity<>(requestDTO);

        // API 호출
        String openAIUrl = "https://api.openai.com/v1/chat/completions";
        try {
            ResponseEntity<OpenAIResponseDTO> responseEntity = restTemplate.exchange(
                    openAIUrl,
                    HttpMethod.POST,
                    requestEntity,
                    OpenAIResponseDTO.class
            );

            OpenAIResponseDTO response = responseEntity.getBody();
            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                String responseStr = response.getChoices().get(0).getMessage().getContent();

                responseStr = responseStr.replaceAll("[\\[\\]]", "").trim();
                String[] result = responseStr.split(",", 2);

                result[0] = result[0].trim();
                result[1] = result[1].trim();
                return result;
            }
            return new String[]{"판단안됨", null};
        } catch (HttpClientErrorException e) {
            throw e;
        }
    }
}