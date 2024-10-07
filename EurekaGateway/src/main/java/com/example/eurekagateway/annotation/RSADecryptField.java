package com.example.eurekagateway.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RSADecryptField {
    // 이 어노테이션은 필드에 사용되어 복호화가 필요한 필드를 표시합니다.
}


/*
해당 필드가 RSA복호화 대상임을 나타냄
@Retention(RetentionPolicy.RUNTIME) -> 런타임중에도 유지되며 reflection을 통해 접근 가능
프로그램이 실행되는 동안 리플렉션을 사용해 해당 어노테이션을 붙은 필드를 찾고 그 필드에 저장된 값을 복호화하는 로직을 적용할 수 있음



 */