package com.example._thecore_back.rest.auth.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class TokenDto {
    private String accessToken; // API 요청에 쓰이는 JWT
    private String refreshToken; // accessToken 갱신에 사용되는 JWT
    private LocalDateTime expiredAt; // 프론트에서 토큰 만료 시점을 알 수 있도록 도와줌
}
