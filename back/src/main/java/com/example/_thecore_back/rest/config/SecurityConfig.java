package com.example._thecore_back.rest.config;

import com.example._thecore_back.rest.auth.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@RequiredArgsConstructor
// 이 클래스는 JWT 인증 필터를 등록하고 요청 경로별 접근 권한을 정의
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final HandlerMappingIntrospector introspector;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // MvcRequestMatcher 생성 시 서블릿 경로 지정 필요
        RequestMatcher apiAuthMatcher = new MvcRequestMatcher(introspector, "/api/auth/**");
        RequestMatcher swaggerUiMatcher = new MvcRequestMatcher(introspector, "/swagger-ui/**");
        RequestMatcher apiDocsMatcher = new MvcRequestMatcher(introspector, "/v3/api-docs/**");

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(apiAuthMatcher).permitAll()
                        .requestMatchers(swaggerUiMatcher).permitAll()
                        .requestMatchers(apiDocsMatcher).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}