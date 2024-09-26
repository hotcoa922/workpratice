package com.example.microserviceuser.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);


    @Around("execution(* com.example.microserviceuser.service.UserServiceImpl.registerUser(..)) ||" +
            "execution(* com.example.microserviceuser.service.UserServiceImpl.login(..))" +
            "xecution(*)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        //ProceedingJoinPoint는 AOP에서 타겟 메서드(Advice가 적용된 메서드)에 대한 정보를 제공하고, 해당 메서드의 실행을 제어하는 기능을 제공

        logger.info("메서드: " + joinPoint.getSignature().getName() + " 가 호출되기 직전인 상태 / 전달된 인자: " + Arrays.toString(joinPoint.getArgs()));

        Object result;
        try {
            result = joinPoint.proceed();  // 메서드 실행
            logger.info("메서드: " + joinPoint.getSignature().getName() + " 가 성공적으로 실행됨 / 반환값: " + result);
        } catch (Throwable ex) {
            logger.error("메서드: " + joinPoint.getSignature().getName() + " 에서 예외가 발생 / 오류 메시지: " + ex.getMessage());
            throw ex;  // 예외를 다시 던짐
        }

        return result;
    }



}
