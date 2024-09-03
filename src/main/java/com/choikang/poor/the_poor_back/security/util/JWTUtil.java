package com.choikang.poor.the_poor_back.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClock;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
@Log4j2
public class JWTUtil {

    @Value("${jwt.util.secretkey}")
    private String secretKey;

    @Value("${jwt.util.expire}")
    private long expire;

    // 토큰 생성
    public String generateToken(String content) throws Exception {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setSubject(content) // jwt에 저장되는 값, 보통 식별 되는 값
                .setExpiration(Date.from(ZonedDateTime.now().plusDays(1).toInstant()))
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                .compact();
    }

    // 토큰으로부터 user 정보 가져오기
    public String getUserInfoFromToken(String tokenStr) throws Exception {
        try {
            Jws<Claims> jwt = Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .setClock(DefaultClock.INSTANCE)
                    .build()
                    .parseClaimsJws(tokenStr);

            Claims claims = jwt.getBody();

            String userInfo = claims.getSubject();
            return userInfo;

        } catch (ExpiredJwtException e) {
            log.error("토큰이 만료되었습니다: " + e.getMessage());
            throw new Exception("토큰이 만료되었습니다", e);
        } catch (JwtException e) {
            log.error("JWT 예외 발생: " + e.getMessage());
            throw new Exception("토큰 파싱 실패", e);
        }
    }

    // 토큰 검증
    public int validateAndExtract(String tokenStr) throws Exception {
        int contentValue = 0;
        try {
            // 토큰 parser 생성 및 서명 검증 -> 토큰의 서명이 유효한지/만료되지 않았는지를 검사함
            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(tokenStr);

            // 클레임을 추출하여 안에 저장된 값 추출
            String subject = jws.getBody().getSubject();

            if (subject != null) {
                contentValue = Integer.parseInt(subject);
            }
        } catch (NumberFormatException e) {
            log.error("ID 형식이 올바르지 않습니다. 에러 내용: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return contentValue;
    }

    // 토큰 재발급
    public String refreshToken(String oldToken) throws Exception {
        try {
            // 토큰 parser 생성 및 서명 검증 -> 토큰의 서명이 유효한지/만료되지 않았는지를 검사함
            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(oldToken);

            // 기존에 있던 토큰에서 클레임을 추출하여 안에 저장된 값 추출
            String subject = jws.getBody().getSubject();

            // 토큰 재생성 후 return
            return generateToken(subject);

        } catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();
            String subject = claims.getSubject();
            return generateToken(subject);
        } catch (JwtException e) {
            throw new Exception("Token refresh failed", e);
        }
    }

    // 토큰 만료 여부 확인
    public boolean isTokenExpired(String token) throws Exception {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token);
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

}
