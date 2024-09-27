package com.example.microserviceboard.util;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@Component
public class JwtTokenProvider {

    private static final Logger logger = Logger.getLogger(JwtTokenProvider.class.getName());

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private SecretKey key;

    // SecretKey 초기화
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        System.out.println("JWT Secret Key(User): " + Base64.getEncoder().encodeToString(key.getEncoded()));  // 테스트용 코드 -> 추후 삭제
    }

    // JWT 토큰 생성
    public String generateToken(String username, List<String> roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // JWT 토큰에서 사용자 이름 추출
    public String getUsernameFromJWT(String token) {
        Claims claims = parseJwt(token);
        return claims != null ? claims.getSubject() : null;
    }

    // JWT 토큰에서 권한 정보 추출
    public List<String> getRolesFromJWT(String token) {
        Claims claims = parseJwt(token);
        return claims != null ? claims.get("roles", List.class) : null;
    }

    // JWT 토큰 검증
    public boolean validateToken(String token) {
        return parseJwt(token) != null;
    }

    // 유저 세부 정보 생성
    public UserDetails getUserDetails(String username, List<String> roles) {
        return User.withUsername(username)
                .password("")
                .authorities(roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList()))
                .build();
    }

    // JWT 토큰 파싱
    private Claims parseJwt(String jwt) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (JwtException ex) {
            logger.warning("JWT validation failed: " + ex.getMessage());
            return null;
        }
    }
}