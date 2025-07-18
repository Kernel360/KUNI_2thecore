package com.example._thecore_back.rest.auth.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

@Slf4j
@Component
// 해당 클래스는 JWT 생성, 검증, 클레임 추출을 전담
public class JwtTokenProvider {

    // JWT 서명을 위한 32자 이상의 비밀키 설정
    private static final String secretKey = "my_secret_key_for_hmac_sha256_algorithm_256bit";
    private final Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

    // JWT 토큰 생성(claims -> JWT에 담을 사용자 정보, expireAt -> 토큰 만료 시간)
    public String generateToken(String subject, Map<String, Object> claims, LocalDateTime expireAt) {
        Date expiration = Date.from(expireAt.atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(subject)           // subject 설정 추가
                .addClaims(claims)             // claims 추가 (roles 등)
                .setIssuedAt(new Date())       // 발급 시간 추가 (iat)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT 유효성 검증
    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder() // 토큰을 파싱하면서 검증 시도
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e){ // 토큰이 만료됐을 시
            log.warn("Token expired: {}", e.getMessage());
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            log.warn("Invalid token: {}", e.getMessage());
        }
        return false;
    }

    // 사용자 ID OR EMAIL 같은 claim에서 subject 추출
    public String getSubject(String token){
        return getClaims(token).getSubject();
    }

    // 모든 claims 가져오기(토큰 파싱 결과에서 JWT의 Payload 정보를 가져옴)
    public Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}