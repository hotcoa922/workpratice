package com.example.eurekagateway.config;

import com.example.eurekagateway.util.RsaUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AppConfig {

    @Value("${rsa.public-key}")
    private String publicKey;

    @Value("${rsa.private-key}")
    private String privateKey;

    @Bean(name = "rsaUtilBean")
    public RsaUtil rsaUtil() throws Exception {
        return new RsaUtil(publicKey, privateKey); // 프로퍼티에서 가져온 키를 사용
    }
}
