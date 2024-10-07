package com.example.microserviceboard.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RSADecrypt {
    Class<?>[] value(); // 복호화 대상 클래스 타입을 지정하는 속성
}

/*
 메소드 실행 시 복호화가 필요한 클래스를 지정
 런타임에 리플렉션으로 접근할 수 있도록 RetentionPolicy.RUNTIME으로 설정

 */

//즉 @RSADecrypt에서 @RSADecryptField가 붙은 필드를 복호화!