package com.example.microserviceuser.util;

import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();

        if (jwtTokenProvider.validateToken(token)) {
            String username = jwtTokenProvider.getUsernameFromJWT(token);

            // JWT에서 권한 정보 가져오기
            var roles = jwtTokenProvider.getRolesFromJWT(token);

            // 유저 정보를 가져와서 Authentication 객체 생성 (권한 정보 포함)
            UserDetails userDetails = jwtTokenProvider.getUserDetails(username, roles); // 권한 정보를 함께 전달
            return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
        }

        return null; // 토큰이 유효하지 않을 경우 인증 실패로 처리
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}