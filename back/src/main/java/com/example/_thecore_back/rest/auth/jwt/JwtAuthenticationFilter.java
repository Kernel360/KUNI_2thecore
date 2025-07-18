package com.example._thecore_back.rest.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
// 헤딩 클래스는 헤더 추출, JWT 유효성 검사, 사용자 식별자 추출, 인증 객체 생성, Spring Security 인증 등록 기능
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 요청 헤더에서 JWT 추출
        String authToken = request.getHeader("Authorization");

        // 헤더가 없거나 Bearer 형식이 아닌 경우
        if (authToken != null && !authToken.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        // "Bearer " 제거 후 토큰만 추출
        String token = authToken.substring(7);

        // 토큰 검증
        if (jwtTokenProvider.validateToken(token)){
            String userId = jwtTokenProvider.getSubject(token);

            // 인증 객체 생성 및 등록
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userId, // userId가 principal
                            null, // 비밀번호는 아직 미검증
                            null // 권한 처리 필요시 여기에 리스트 전달
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            // 인증 객체를 SecurityContext에 등록
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }

        // 다음 필터로 넘김
        filterChain.doFilter(request, response);
    }
}
