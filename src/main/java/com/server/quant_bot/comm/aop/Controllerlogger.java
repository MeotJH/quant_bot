package com.server.quant_bot.comm.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class Controllerlogger {

    //controller 패키지에 있는 모든 메소드들
    @Pointcut("execution(* com.server.quant_bot..controller..*(..))")
    private void cut(){}

    @Around("cut()")
    public Object controllerArgsLogger(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("In Controller");
        Signature signature = joinPoint.getSignature();

        log.info(signature.getDeclaringTypeName() + "." + signature.getName());
        Object[] args = joinPoint.getArgs();

        for (Object each: args){
            log.info(each.toString());
        }
        return joinPoint.proceed();
    }
}
