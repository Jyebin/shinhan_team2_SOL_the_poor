package com.choikang.poor.the_poor_back.security.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JWTUtilTests {
    @Autowired
    private JWTUtil jwtUtil;

    @Test
    void testGenerateAndValidateToken() throws Exception {
        String content = "testUserTokenValues"; // 테스트 값, 사용자 정보가 들어감
        String token = jwtUtil.generateToken(content); // 토큰 생성
        assertNotNull(token); // 토큰 값이 null인지 검사

        // 토큰으로부터 user 정보 추출
        String extractedContent = jwtUtil.getUserInfoFromToken(token);
        // 추출한 정보가 입력된 정보와 일치하는지
        assertEquals(content, extractedContent);
    }

    @Test
    void testRefreshToken() throws Exception{
        String content = "testUserTokenValues";
        String oldToken = jwtUtil.generateToken(content); // 토큰 생성
        Thread.sleep(1000); // 1초 대기

        // 기존 토큰과 재발급된 토큰이 동일한지 확인
        String newToken = jwtUtil.refreshToken(oldToken);
        assertEquals(oldToken, newToken);

        // 재발급 토큰값이 올바른 값을 저장하였는지 확인
        String extractedContent = jwtUtil.getUserInfoFromToken(newToken);
        assertNotEquals(content, extractedContent);
    }

    @Test
    void testExperienceToken() throws Exception{
        JWTUtil shortExpirationJwtUtil = new JWTUtil();
        // 1분 후 토큰이 만료되도록 설정
        shortExpirationJwtUtil.setExpire(1);

        String content = "testUserTokenValues";
        String token = shortExpirationJwtUtil.generateToken(content);

        // 토큰이 만료되었는지 확인
        assertFalse(shortExpirationJwtUtil.isTokenExpired(token));

    }
}