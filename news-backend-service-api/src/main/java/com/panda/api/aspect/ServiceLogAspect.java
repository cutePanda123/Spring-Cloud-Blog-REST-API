package com.panda.api.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceLogAspect {
    final static Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    @Around("execution(* com.panda.*.service.impl..*.*(..))")
    public Object durationOfServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info(
                "======== start execution service.method: {}.{}=========",
                joinPoint.getTarget().getClass(),
                joinPoint.getSignature().getName());
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();
        long duration = end - start;
        if (duration > 3000) {
            logger.error("execution time: {}", duration);
        } else if (duration > 2000) {
            logger.error("execution time: {}", duration);
        } else {
            logger.error("execution time: {}", duration);
        }
        return result;
    }
}
