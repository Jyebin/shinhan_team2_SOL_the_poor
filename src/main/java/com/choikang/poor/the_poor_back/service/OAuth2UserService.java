package com.choikang.poor.the_poor_back.service;

import com.choikang.poor.the_poor_back.dto.KakaoUserDTO;
import com.choikang.poor.the_poor_back.model.User;
import com.choikang.poor.the_poor_back.repository.UserRepository;
import com.choikang.poor.the_poor_back.security.util.JWTUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
@Lazy
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientID;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectURL;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;

    public String kakaoLogin(String code) throws Exception {
        String accessToken = getKakaoAccessToken(code);
        KakaoUserDTO kakaoUserDTO = getKakaoUserInfo(accessToken);

        // 사용자 정보를 바탕으로 User 엔티티 저장 또는 업데이트
        User user = saveOrUpdateUser(kakaoUserDTO);

        // user id를 통해서 JWT 토큰 생성
        return jwtUtil.generateToken(String.valueOf(user.getUserID()));
    }

    // 사용자 인증 수단 (액세스 토큰)가져옴
    public String getKakaoAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        // 요청 파라미터 설정
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", "authorization_code");
        parameters.add("client_id", kakaoClientID);
        parameters.add("client_secret", kakaoClientSecret); // 클라이언트 시크릿 추가
        parameters.add("redirect_uri", redirectURL); // 등록된 리다이렉트 URI 확인
        parameters.add("code", code);

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("charset", "utf-8");

        // 클라이언트 자격증명을 Basic Auth 헤더에 추가
        String credentials = kakaoClientID + ":" + kakaoClientSecret;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        headers.add("Authorization", "Basic " + encodedCredentials);

        // 요청 엔티티 생성
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);

        try {
            // POST 요청을 보내 액세스 토큰을 얻음
            ResponseEntity<String> response = restTemplate.postForEntity("https://kauth.kakao.com/oauth/token", request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                if (responseBody != null) {
                    // JSON 응답 파싱
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode root = objectMapper.readTree(responseBody);
                    JsonNode accessTokenNode = root.path("access_token");
                    if (!accessTokenNode.isMissingNode()) {
                        String accessToken = accessTokenNode.asText();
                        System.out.println("Access Token: " + accessToken); // 실제 사용 시 로그로 대체
                        return accessToken;
                    } else {
                        System.err.println("액세스 토큰 필드가 응답에 없습니다.");
                    }
                } else {
                    System.err.println("응답 본문이 null입니다.");
                }
            } else {
                System.err.println("액세스 토큰을 가져오는 중 오류 발생: " + response.getStatusCode()); // 실제 사용 시 로그로 대체
            }
        } catch (Exception e) {
            System.err.println("액세스 토큰을 가져오는 중 오류 발생: " + e.getMessage());
        }
        return "Error";
    }

    // 카카오에서 엑세스 토큰을 통해 유저 정보 가져옴
    public KakaoUserDTO getKakaoUserInfo(String accessToken) {
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        RestTemplate restTemplate = new RestTemplate();

        // HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        // 요청 헤더를 포함한 HttpEntity 생성
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            // GET 요청 보내기
            ResponseEntity<Map> response = restTemplate.exchange(reqURL, HttpMethod.GET, entity, Map.class);

            // 응답 처리
            Map<String, Object> body = response.getBody();
            System.out.println(body);
            if (body != null) {
                Map<String, Object> account = (Map<String, Object>) body.get("kakao_account");

                String email = (String) account.get("email");
                String name = (String) account.get("name");

                return new KakaoUserDTO(name, email);
            } else {
                throw new RuntimeException("Kakao API 응답 본문이 null입니다.");
            }
        } catch (Exception e) {
            System.err.println("Kakao API 호출 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("Kakao API 호출 중 오류 발생", e);
        }
    }

    // 테이블에 유저 정보 저장
    private User saveOrUpdateUser(KakaoUserDTO kakaoUserDTO) {
        Optional<User> existingUser = userRepository.findByUserEmail(kakaoUserDTO.getEmail());

        // DB에 USER가 존재하면 해당 유저 return 아니면 새로 저장
        if (existingUser.isPresent()) {
            return existingUser.get();
        } else {
            // 새로운 사용자 생성
            User newUser = User.builder()
                    .userName(kakaoUserDTO.getName())
                    .userEmail(kakaoUserDTO.getEmail())
                    .build();
            return userRepository.save(newUser);
        }
    }
}

