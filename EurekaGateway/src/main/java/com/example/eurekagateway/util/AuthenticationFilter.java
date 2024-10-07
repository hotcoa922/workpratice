package com.example.eurekagateway.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Configuration
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Authorization 헤더에서 토큰 추출
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtTokenProvider.validateToken(token)) {
                // 토큰이 유효한 경우
                String username = jwtTokenProvider.getUsernameFromJWT(token);
                String email = jwtTokenProvider.getEmailFromJWT(token); // 이메일 추출
                List<String> roles = jwtTokenProvider.getRolesFromJWT(token);

                // 헤더에 사용자 정보 추가
                exchange = exchange.mutate().request(
                        exchange.getRequest().mutate()
                                .header("X-User-Username", username)
                                .header("X-User-Email", email) // 이메일 헤더 추가
                                .header("X-User-Roles", String.join(",", roles))
                                .build()
                ).build();

                return chain.filter(exchange);
            } else {
                // 토큰 검증 실패 시
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        } else {
            // 토큰이 없는 경우
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return -1; // 다른 필터보다 우선순위를 높게 설정
    }
}
