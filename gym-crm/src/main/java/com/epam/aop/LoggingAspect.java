package com.epam.aop;

import com.epam.utility.TransactionId;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(public * com.epam.service.*.*(..))")
    public void serviceMethods() {}

    @Pointcut("execution(* com.epam.controller..*(..))")
    public void controllerMethods() {}

    @Around("controllerMethods()")
    public Object logRestCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String transactionId = TransactionId.getTransaction();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = method.getName();
        String httpMethod = getHttpMethod(method);

        Object[] args = joinPoint.getArgs();
        String sanitizedArgs = maskSensitiveData(Arrays.toString(args));

        log.info("[{}] REST Call - {} {}.{}", transactionId, httpMethod, className, methodName);
        log.info("[{}] Request Data: {}", transactionId, sanitizedArgs);

        Object response = joinPoint.proceed();
        String sanitizedResponse = maskSensitiveData(response.toString());

        log.info("[{}] Response: {}", transactionId, sanitizedResponse);

        return response;
    }

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

    private String getHttpMethod(Method method) {
        if (method.isAnnotationPresent(GetMapping.class)) return "GET";
        if (method.isAnnotationPresent(PostMapping.class)) return "POST";
        if (method.isAnnotationPresent(PutMapping.class)) return "PUT";
        if (method.isAnnotationPresent(DeleteMapping.class)) return "DELETE";
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping.method().length > 0) {
                return requestMapping.method()[0].name();
            }
        }
        return "";
    }

    private String maskSensitiveData(String data) {
        return data.replaceAll("(?<=password=|oldPassword=)([^,\\]]+)", "******");
    }
}
