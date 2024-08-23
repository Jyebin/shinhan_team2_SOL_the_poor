package com.choikang.poor.the_poor_back.security.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import lombok.extern.log4j.Log4j2;

import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JWTUtil {
    // JWT 암호화 키, 32자, 256bit (8 * 32 = 256)
    private String secretKey = "choikang2teamjwtsecretsolthepoor"; // 이부분 환경 변수로 나중에 바꾸기

    // 만료 시간 30분
    private long expire = 30;

    // 토큰 생성
    public String generateToken(String content) throws Exception{
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant())) // 만료 시간
                .claim("sub", content) // jwt에 저장되는 값, 보통 식별 되는 값
                .signWith(SignatureAlgorithm.ES256, secretKey.getBytes("UTF-8"))
                .compact();
    }

    // 토큰 검증 및 값 추출
    public int validateAndExtract(String tokenStr) throws Exception{
        int contentValue = 0;
        try{
            // 토큰 parser 생성 및 서명 검증 -> 토큰의 서명이 유효한지/만료되지 않았는지를 검사함
            DefaultJws defaultJws = (DefaultJws) Jwts.parser()
                    .setSigningKey(secretKey.getBytes("UTF-8")).build().parseClaimsJws(tokenStr);

            // 클레임을 추출하여 안에 저장된 값 추출
            DefaultClaims claims = (DefaultClaims) defaultJws.getBody();
            String subject = claims.getSubject();
            if(subject != null){
                contentValue = Integer.parseInt(subject);
            }
        }  catch (NumberFormatException e){
            log.error("ID 형식이 올바르지 않습니다. 에러 내용: " + e.getMessage());
        } catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return contentValue;
    }
}
