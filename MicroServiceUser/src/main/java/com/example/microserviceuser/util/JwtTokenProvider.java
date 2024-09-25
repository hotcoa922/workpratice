package com.example.microserviceuser.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
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

    // JWT 토큰 생성
    public String generateToken(String username, List<String> roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles) // 권한 정보를 클레임에 추가
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    // JWT 토큰에서 사용자 이름 추출
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // JWT 토큰에서 권한 정보 추출
    public List<String> getRolesFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return (List<String>) claims.get("roles");
    }

    // JWT 토큰 검증
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.warning("Invalid JWT signature: " + ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.warning("Invalid JWT token: " + ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.warning("Expired JWT token: " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.warning("Unsupported JWT token: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.warning("JWT claims string is empty: " + ex.getMessage());
        }
        return false;
    }


    // 유저 세부 정보를 가져오는 메소드
    public UserDetails getUserDetails(String username, List<String> roles) {
        return User.withUsername(username)
                .password("") // 비밀번호는 이미 검증된 후 토큰 발급 과정이므로 빈 값
                .authorities(roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // SimpleGrantedAuthority로 변환
                        .collect(Collectors.toList()))
                .build();
    }
}
