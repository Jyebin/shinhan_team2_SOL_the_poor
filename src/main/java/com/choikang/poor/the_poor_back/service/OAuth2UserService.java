package com.choikang.poor.the_poor_back.service;

import com.choikang.poor.the_poor_back.dto.KakaoUserDTO;
import com.choikang.poor.the_poor_back.model.Account;
import com.choikang.poor.the_poor_back.model.User;
import com.choikang.poor.the_poor_back.repository.AccountRepository;
import com.choikang.poor.the_poor_back.repository.UserRepository;
import com.choikang.poor.the_poor_back.security.util.JWTUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
    private final AccountRepository accountRepository;
    private final JWTUtil jwtUtil;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientID;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectURL;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;


    // 카카오 로그인, JWT 생성 후 return
    public String kakaoLogin(String code) throws Exception {
        String accessToken = getKakaoAccessToken(code);
        KakaoUserDTO kakaoUserDTO = getKakaoUserInfo(accessToken);

        // 사용자 정보를 바탕으로 User 엔티티 저장 또는 업데이트
        User user = saveOrUpdateUser(kakaoUserDTO);

        // user id를 통해서 JWT 토큰 생성
        return jwtUtil.generateToken(user.getUserID() + ":" + accessToken);
    }

    // 카카오 개발자 도구에 POST 요청하여 사용자 인증 수단 (액세스 토큰)가져옴
    public String getKakaoAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        // 요청 파라미터 설정
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", "authorization_code");
        parameters.add("client_id", kakaoClientID);
        parameters.add("client_secret", kakaoClientSecret);
        parameters.add("redirect_uri", redirectURL);
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
                        return accessTokenNode.asText();
                    } else {
                        log.error("액세스 토큰 필드가 응답에 없습니다.");
                    }
                } else {
                    log.error("응답 본문이 null입니다.");
                }
            } else {
                log.error("액세스 토큰을 가져오는 중 오류 발생: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("액세스 토큰을 가져오는 중 오류 발생: " + e.getMessage());
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
            log.error("Kakao API 호출 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("Kakao API 호출 중 오류 발생", e);
        }
    }

    // DB에 사용자 정보 저장
    private User saveOrUpdateUser(KakaoUserDTO kakaoUserDTO) {
        Optional<User> existingUser = userRepository.findByUserEmail(kakaoUserDTO.getEmail());

        // DB에 사용자 존재하면 해당 사용자 정보 return, 아니면 새로 저장
        if (existingUser.isPresent()) {
            return existingUser.get();
        } else {
            // 새로운 사용자 생성
            User newUser = User.builder()
                    .userName(kakaoUserDTO.getName())
                    .userEmail(kakaoUserDTO.getEmail())
                    .build();
            User savedUser = userRepository.save(newUser);

            // 사용자 생성 시 더미 계좌 추가
            insertDummyAccounts(savedUser);

            return savedUser;
        }
    }

    private void insertDummyAccounts(User user) {
        String accountNumber1 = generateUniqueAccountNumber();
        String accountNumber2 = generateUniqueAccountNumber();

        Account account1 = Account.builder()
                .accountBalance(123456)
                .accountPW(1111)
                .accountName("쏠편한 입출금통장(저축예금)")
                .accountNumber(accountNumber1)
                .accountHasCan(false)
                .user(user)
                .build();
        accountRepository.save(account1);

        Account account2 = Account.builder()
                .accountBalance(22222)
                .accountPW(2222)
                .accountName("[금융거래한도계좌1] 신한 청년 DREAM 통장")
                .accountNumber(accountNumber2)
                .accountHasCan(false)
                .user(user)
                .build();
        accountRepository.save(account2);
    }

    private String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = "110-" + (int)(Math.random() * 900 + 100) + "-" + (int)(Math.random() * 9000 + 1000);
        } while (accountRepository.findByAccountNumber(accountNumber).isPresent());

        return accountNumber;
    }

    // 쿠키로 부터 JWT 값 가져오기
    public String getJWTFromCookies(HttpServletRequest request){
        String token = null;
        for(Cookie cookie : request.getCookies()){
            if(cookie.getName().equals("token")){
                token = cookie.getValue();
            }
        }
        return token;
    }

    // 쿠키 삭제
    public Cookie deleteJWTFromCookie(){
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setValue(null);
        return cookie;
    }

    // JWT로부터 user 정보 가져오기
    public String getUserInfo(String token) throws Exception {
        String userInfo = jwtUtil.getUserInfoFromToken(token);
        Optional<User> userOptional = userRepository.findById(Long.valueOf(userInfo.split(":")[0]));

        if (userOptional.isPresent()) {
            return userInfo;
        } else {
            throw new RuntimeException("유효하지 않은 사용자 정보입니다.");
        }
    }

    // JWT로부터 user id 가져오기
    public Long getUserIDFromJWT(String token) throws Exception {
        return Long.parseLong(getUserInfo(token).split(":")[0]);
    }

    // JWT로부터 user access token 가져오기
    public String getUserAccessTokenFromJWT(String token) throws Exception {
        return getUserInfo(token).split(":")[1];
    }

    // JWT가 유효한지 검사하고 만일 만료되었을 시 재발급 하기
    public String validateTokenAndRegenerate(HttpServletRequest request) throws Exception {
        String token = getJWTFromCookies(request);
        try {
            if (jwtUtil.isTokenExpired(token)) {
                String newToken = jwtUtil.refreshToken(token);
                return newToken;
            }
        } catch (ExpiredJwtException e) {
            try {
                String newToken = jwtUtil.refreshToken(token);
                return newToken;
            } catch (Exception ex) {
                throw new Exception("Token validation failed", ex);
            }
        }
        return token;
    }

    // 카카오 로그아웃 api get 요청
    public void kakaoLogout(String token) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String logoutUrl = "https://kapi.kakao.com/v1/user/logout";

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + getUserAccessTokenFromJWT(token));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(logoutUrl, HttpMethod.GET, requestEntity, Map.class);

        if(!response.getStatusCode().is2xxSuccessful()){
            throw new RuntimeException("Failed logout from kakao");
        }
    }

}

