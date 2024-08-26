package com.choikang.poor.the_poor_back.dto;

import com.choikang.poor.the_poor_back.model.User;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Data
@Log4j2
public class AuthUserDTO implements OAuth2User {

    private static final String DEFAULT_ROLE = "ROLE_USER";
    private String email;
    private String name;
    private Map<String, Object> attr;

    public AuthUserDTO(String email, String name, Map<String, Object> attr) {
        this.email = email;
        this.name = name;
        this.attr = attr;
    }

    public AuthUserDTO(User user, Map<String, Object> attr) {
        this.email = user.getUserEmail();
        this.name = user.getUserName();
        this.attr = attr;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attr;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(DEFAULT_ROLE));
    }

    @Override
    public String getName() {
        return this.email;
    }
}
