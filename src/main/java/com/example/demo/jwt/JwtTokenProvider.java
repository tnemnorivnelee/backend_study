package com.example.demo.jwt;

import com.example.demo.common.Role;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;

    public JwtTokenProvider(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith(secretKey) // 서명을 검증할 키 설정
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("username", String.class);
    }

    public String getEmail(String token) {
        return Jwts.parser()
                .verifyWith(secretKey) // 서명을 검증할 키 설정
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("email", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().
                verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            // 서명과 유효성을 검증하면서 파싱하여 Claims 객체를 생성
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true; // 토큰이 유효함
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token format: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT token: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT token: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: " + e.getMessage());
        }
        return false; // 위의 예외 중 하나라도 발생한 경우 토큰이 유효하지 않음
    }

    public String createJwt(String email, String role, Long expiredMs) {
        System.out.println("createJwt : " + role);
        return Jwts.builder()
                .claim("email", email)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs)) // 토큰 만료일 설정
                .signWith(secretKey) // 암호화 방식 설정
                .compact();
    }
}
