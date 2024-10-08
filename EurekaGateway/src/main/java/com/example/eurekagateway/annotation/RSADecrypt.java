package com.example.eurekagateway.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RSADecrypt {
    Class<?>[] value(); // 복호화된 데이터를 매핑할 클래스
}

/*
 메소드 실행 시 복호화가 필요한 클래스를 지정
 런타임에 리플렉션으로 접근할 수 있도록 RetentionPolicy.RUNTIME으로 설정

 */

