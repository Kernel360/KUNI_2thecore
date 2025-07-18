package com.example._thecore_back;

import com.example._thecore_back.rest.auth.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void generateAndValidateTokenTest() {
        // given
        String subject = "adminUser1";
        Map<String, Object> claims = Map.of(
                "roles", List.of("ADMIN", "USER")
        );

        LocalDateTime expiry = LocalDateTime.now().plusMinutes(15);

        // when
        String token = jwtTokenProvider.generateToken(subject, claims, expiry);

        // then
        assertThat(token).isNotBlank();
        assertThat(jwtTokenProvider.validateToken(token)).isTrue();

        Claims parsedClaims = jwtTokenProvider.getClaims(token);
        assertThat(parsedClaims.getSubject()).isEqualTo("adminUser1");
        assertThat(parsedClaims.get("roles", List.class)).containsExactly("ADMIN", "USER");

        System.out.println("✅ Generated Token: " + token);
    }

}
