package com.example.eurekagateway.aspect;


import com.example.eurekagateway.annotation.RSADecrypt;
import com.example.eurekagateway.annotation.RSADecryptField;
import com.example.eurekagateway.util.RsaUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Aspect
@Primary    //우선순위 지정
@Component
public class EncryptionAspect {

    @Autowired
    private RsaUtil rsaUtil;

    // 메서드 실행 전 -> RSA 복호화 로직을 수행
    @Before(value = "@annotation(com.example.eurekagateway.annotation.RSADecrypt)")
    public void RSAStringDecrypt(JoinPoint joinPoint) throws Exception {

        // 메서드에 전달된 모든 인자 리스트를 가져옴
        List<Object> args = Arrays.stream(joinPoint.getArgs()).collect(Collectors.toList());

        // 호출된 메서드의 메타정보를 가져옴
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        // RSADecrypt 어노테이션에 명시된 클래스 타입을 가져옴 (복호화 대상 클래스)
        List<Class> targetClassList = Arrays.stream(methodSignature.getMethod().getAnnotation(RSADecrypt.class).value()).collect(Collectors.toList());

        // 메서드 인자 중에서 복호화 대상 클래스 타입인 인자를 필터링
        List<Object> filterArgs = args.stream()
                .filter(arg -> targetClassList.containsAll(Collections.singleton(arg.getClass())))
                .collect(Collectors.toList());


        // 필터링된 인자에 대해 복호화 로직 수행
        for (Object arg : filterArgs) {
            // 클래스 내에서 @RSADecryptField 어노테이션이 붙은 필드 추출
            List<Field> filterFields = Arrays.stream(arg.getClass().getDeclaredFields())
                    .filter(field -> Arrays.stream(field.getDeclaredAnnotations())
                            .anyMatch(annotation -> annotation.annotationType().equals(RSADecryptField.class)))
                    .collect(Collectors.toList());

            for (Field field : filterFields) {
                // 해당 필드의 getter, setter 메서드를 찾음
                String targetMethodName = toUppercase(field.getName());

                List<Method> methods = Arrays.stream(arg.getClass().getDeclaredMethods()).collect(Collectors.toList());
                Optional<Method> targetGetMethod = methods.stream().filter(method -> method.getName().equals("get" + targetMethodName)).findFirst();
                Optional<Method> targetSetMethod = methods.stream().filter(method -> method.getName().equals("set" + targetMethodName)).findFirst();

                if (targetGetMethod.isPresent() && targetSetMethod.isPresent()) {
                    // getter로 암호화된 값을 읽고 복호화
                    String plainText = (String) targetGetMethod.get().invoke(arg, null);
                    if (!StringUtils.isBlank(plainText)) {
                        String decryptText = rsaUtil.decrypt(plainText);
                        targetSetMethod.get().invoke(arg, decryptText); // 복호화된 값을 setter로 다시 설정
                    }
                }
            }
        }
    }

    // 필드 이름을 대문자로 변환하여 getter/setter 이름에 맞춤
    private static String toUppercase(String targetStr) {
        return targetStr.length() > 1 ? targetStr.substring(0, 1).toUpperCase() + targetStr.substring(1) : targetStr.substring(0, 1);
    }


}
