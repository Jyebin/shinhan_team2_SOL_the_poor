package com.choikang.poor.the_poor_back.dto;

import com.choikang.poor.the_poor_back.model.User;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Log4j2
public class AuthUserDTO implements OAuth2User {
    // 서비스 내에서 따로 user의 역할이 구분되지 않으므로 임의로 설정
    private static final String DEFAULT_ROLE = "ROLE_USER";

    // 사용하는 user 데이터
    private String email;
    private String name;
    private Map<String, Object> attr;

    // 생성자
    // 생성자를 단순화하여 소셜 로그인에 필요한 정보만을 받음.
    public AuthUserDTO(String email, String name, Map<String, Object> attr) {
        this.email = email;
        this.name = name;
        this.attr = attr;
    }

    // User 엔티티를 기반으로 하는 생성자 추가
    public AuthUserDTO(User user, Map<String, Object> attr) {
        this.email = user.getUserEmail();
        this.name = user.getUserName();
        this.attr = attr;
    }
    
    @Override
    public Map<String, Object> getAttributes() {
        return this.attr;
    }

    // 모든 역할을 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(DEFAULT_ROLE));
    }

}