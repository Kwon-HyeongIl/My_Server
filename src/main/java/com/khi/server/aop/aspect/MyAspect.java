package com.khi.server.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class MyAspect {

    /*
     * 포인트컷 지정에서 AopConfig 클래스를 제외하지 않을 경우, 자기 자신인 LogAspect를 생성하는 메서드도 Aop로 처리하게 되므로, 순환 참조에 빠지게 됨
     * (+LogAspect에 @Component 어노테이션을 붙여서 컴포넌트 스캔으로 빈을 등록해도 문제가 해결됨)
     */
    @Around("execution(* com.khi.server..*(..)) && !target(com.khi.server.aop.configuration.AopConfig)")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {

        String[] fullLogArr = String.valueOf(joinPoint.getSignature()).split("\\.");
        String classType = fullLogArr[fullLogArr.length-2];
        String methodType = fullLogArr[fullLogArr.length-1].split("\\(")[0];

        log.info("[MyLog] " + "(" + classType + ") " + methodType + " 메서드 호출");

        return joinPoint.proceed();
    }
}

