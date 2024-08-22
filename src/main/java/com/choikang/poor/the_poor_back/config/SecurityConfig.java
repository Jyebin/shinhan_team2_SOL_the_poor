package com.choikang.poor.the_poor_back.config;

import com.choikang.poor.the_poor_back.service.OAuth2UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
@Log4j2
@EnableMethodSecurity
public class SecurityConfig {
    private final OAuth2UserService oAuth2UserService;

    public SecurityConfig(OAuth2UserService oAuth2UserService) {
        this.oAuth2UserService = oAuth2UserService;
    }

    @Bean(name = "customSecurityFilterChain")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 토큰 비활성화
        http.csrf(csrf -> csrf.disable());


        // 로그인페이지 외, 로그인하지 않은 상태일 시 접근 불가
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated()
        );

        // 로그인 기능 설정
        http.oauth2Login(oaUth2Configuer -> oaUth2Configuer
                .loginPage("/login") // 로그인 페이지 경로 설정
                .successHandler(successHandler()) // 로그인 성공 시 실행되는 핸들러
                .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))) // OAuth2 제공자로 부터 받은 사용자 정보를 처리하는 커스텀 시스템 지정. 사용자 정보를 애플리케이션의 사용자 모델로 변환하거나 추가적인 사용자 정보 처리 로직을 수행할 수 있음
                .formLogin(form -> form.disable()); // 폼 로그인 비활성화

        // (이전 페이지가 없는 경우) 로그인 뒤 메인 페이지로 이동
        http.formLogin(c -> {
            c.defaultSuccessUrl("/");
        });

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return ((request, response, authentication) -> {
            DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

            String id = defaultOAuth2User.getAttributes().get("id").toString();
            String body = """
                        {"id":"%s"}
                    """.formatted(id);

            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());

            PrintWriter writer = response.getWriter();
            writer.println(body);
            writer.flush();

            response.sendRedirect("/");
        });
    }

}
