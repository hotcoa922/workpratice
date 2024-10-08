package com.example.eurekagateway.config;

import com.example.eurekagateway.util.RsaUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;


@Configuration
@EnableWebFlux
public class AppConfig {

    @Autowired
    private RsaUtil rsaUtil;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @PostConstruct
    public void exposePublicKeyEndpoint() {
        handlerMapping.registerMapping(
                RequestMappingInfo.paths("/rsa/publicKey")
                        .methods(RequestMethod.GET)
                        .produces(MediaType.TEXT_PLAIN_VALUE)
                        .build(),
                this,
                ReflectionUtils.findMethod(AppConfig.class, "getPublicKey")
        );
    }

    public Mono<ResponseEntity<String>> getPublicKey() {
        String publicKey = rsaUtil.getPublicKey();
        return Mono.just(ResponseEntity.ok(publicKey));
    }
}