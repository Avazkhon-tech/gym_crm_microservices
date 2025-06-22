package com.epam.trainerworkloadservice.aop;

import com.epam.trainerworkloadservice.utility.TransactionId;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(public * com.epam.trainerworkloadservice.service..*.*(..))")
    public void serviceMethods() {}


    @Around("serviceMethods()")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String transactionId = TransactionId.getTransaction();

        Object[] args = joinPoint.getArgs();
        String sanitizedArgs = maskSensitiveData(Arrays.toString(args));

        log.info("[{}] Executing: {}.{}() with arguments: {}",
                transactionId,
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                sanitizedArgs);

        try {
            return joinPoint.proceed();
        } catch (Exception ex) {
            log.debug("[{}] Exception in {}.{}() with message: {}",
                    transactionId,
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    ex.getMessage());
            throw ex;
        } finally {
            log.info("[{}] Method {}.{}() executed",
                    transactionId,
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName());
        }
    }

    private String maskSensitiveData(String data) {
        return data.replaceAll("(?<=password=|oldPassword=)([^,\\]]+)", "******");
    }
}
