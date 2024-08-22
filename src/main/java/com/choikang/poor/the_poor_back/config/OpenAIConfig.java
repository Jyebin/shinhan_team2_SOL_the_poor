package com.choikang.poor.the_poor_back.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

//OpenAI API와 통신하기 위한 메소드
@Configuration
public class OpenAIConfig {

    @Value("${openai.api.key}")
    private String openAIKey;

    @Bean(name = "openAIRestTemplate")
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            String apiKey = "Bearer " + openAIKey;
            System.out.println("키값이에요 키값" + apiKey);
            headers.add("Authorization", apiKey);
            headers.add("Content-Type", "application/json");
            return execution.execute(request, body);
        }));
        return restTemplate;
    }
}
