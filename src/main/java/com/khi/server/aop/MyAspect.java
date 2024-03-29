package com.khi.server.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class MyAspect {
    /*
     * Configuration을 따로 구성한 상태의 포인트컷 지정에서 AopConfig 클래스를 제외하지 않을 경우,
     * 자기 자신인 LogAspect를 생성하는 메서드도 Aop로 처리하게 되므로, 순환 참조에 빠지게 됨
     * (+MyAspect에 @Component 어노테이션을 붙여서 컴포넌트 스캔으로 빈을 등록해도 문제가 해결됨)
     */

    @Around("execution(* com.khi.server..*(..))")
    public Object classMethodLog(ProceedingJoinPoint joinPoint) throws Throwable {

        String[] fullLogArr = String.valueOf(joinPoint.getSignature()).split("\\.");
        String className = fullLogArr[fullLogArr.length-2];
        String methodName = fullLogArr[fullLogArr.length-1].split("\\(")[0];

        log.info("[MethodLog] " + "(" + className + ") " + methodName + " 메서드 호출");

        return joinPoint.proceed();
    }

    @AfterThrowing(value = "execution(* com.khi.server..*(..))", throwing = "e")
    public void exLog(JoinPoint joinPoint, Exception e) {

        String[] fullLogArr = String.valueOf(joinPoint.getSignature()).split("\\.");
        String className = fullLogArr[fullLogArr.length-2];
        String methodName = fullLogArr[fullLogArr.length-1].split("\\(")[0];

        log.info("[ExLog] " + "(" + className + ") " + methodName + " 메서드에서 예외 발생, 예외 메시지: {}", e.getMessage());
    }
}

