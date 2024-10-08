package com.example.eurekagateway.filter;

import com.example.eurekagateway.util.RsaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/*
클라이언트로 부터 오는 요청 가로대서 암호화된 본문을 복호화
 */
@Component
@Configuration
public class EncryptionFilter implements GlobalFilter, Ordered {

    @Autowired
    private RsaUtil rsaUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 요청이 POST 또는 PUT 메서드인지 확인
        if (exchange.getRequest().getMethod().matches("POST|PUT")) {
            return exchange.getRequest().getBody().collectList().flatMap(dataBufferList -> {
                // 요청 본문 읽기
                String encryptedBody = dataBufferList.stream()
                        .map(dataBuffer -> {
                            byte[] bytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bytes);
                            return new String(bytes, StandardCharsets.UTF_8);
                        })
                        .reduce("", String::concat);

                try {
                    // 암호화된 본문 복호화
                    String decryptedBody = rsaUtil.decrypt(encryptedBody);

                    // 복호화된 본문으로 새로운 요청 생성
                    ServerWebExchange mutatedExchange = exchange.mutate()
                            .request(exchange.getRequest().mutate()
                                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                    .build())
                            .build();

                    // 새로운 본문으로 요청 전달
                    return chain.filter(mutatedExchange);
                } catch (Exception e) {
                    // 복호화 실패 시 처리
                    exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                    return exchange.getResponse().setComplete();
                }
            });
        } else {
            // GET 등의 메서드는 그대로 전달
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -2; // AuthenticationFilter보다 우선순위를 높게 설정
    }
}