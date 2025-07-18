package com.example._thecore_back.rest.auth.controller;

import com.example._thecore_back.rest.auth.jwt.JwtTokenProvider;
import com.example._thecore_back.rest.auth.model.LoginRequest;
import com.example._thecore_back.rest.auth.model.RefreshRequest;
import com.example._thecore_back.rest.auth.model.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequest request){
        // 인증 시도
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 인증된 사용자 정보
        String email = authentication.getName();

        // claims 생성
        Map<String, Object> claims = Map.of("email", email);
        LocalDateTime accessExpireAt = LocalDateTime.now().plusMinutes(30);
        LocalDateTime refreshExpireAt = LocalDateTime.now().plusDays(7);

        // 토큰 발급
        String accessToken = jwtTokenProvider.generateToken(email, claims, accessExpireAt);
        String refreshToken = jwtTokenProvider.generateToken(email, claims, refreshExpireAt);

        return ResponseEntity.ok(TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiredAt(accessExpireAt)
                .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> refresh(@RequestBody RefreshRequest request){
        if (!jwtTokenProvider.validateToken(request.getRefreshToken())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtTokenProvider.getSubject(request.getRefreshToken());

        Map<String, Object> claims = Map.of("email", email);
        LocalDateTime accessExpireAt = LocalDateTime.now().plusMinutes(30);

        String accessToken = jwtTokenProvider.generateToken(email, claims, accessExpireAt);

        return ResponseEntity.ok(TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(request.getRefreshToken())
                .expiredAt(accessExpireAt)
                .build());
    }
}
