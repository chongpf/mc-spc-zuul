package com.chong.mcspczuul.aspect;

import com.chong.mcspczuul.annotation.SetOperationLimit;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

@Aspect
@Component
public class SetOperationLimitAspect {

    private static final Logger logger = LoggerFactory.getLogger(SetOperationLimitAspect.class);

    public static volatile Map<String, Semaphore> semphoreMap = new ConcurrentHashMap<>();

    @Pointcut(value = "execution(* com.chong.mcspczuul.controller.*.*(..))")
    private void pointCut() {
    }

    @Around(value = "pointCut()")
    public Object arroud(ProceedingJoinPoint proceedingJoinPoint) {

        Object obj = null;
        try {
            logger.info("enter api operation limit aspect");
            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
            if (signature.getMethod().isAnnotationPresent(SetOperationLimit.class)) {
                logger.info("operation limit start:"+signature.getName());
                SetOperationLimit annotaiton = signature.getMethod().getAnnotation(SetOperationLimit.class);
                String key = StringUtils.isBlank(annotaiton.value()) ? "defaultLimit" : annotaiton.value();
                Semaphore semaphore = semphoreMap.get(key);
                try {
                    semaphore.acquire();
                    obj = proceedingJoinPoint.proceed();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    semaphore.release();
                    logger.info("operation limit end");
                }
            }else{
                //没有设置@ApiOperationLimit时，直接执行方法.
                obj = proceedingJoinPoint.proceed();
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        logger.info("leave api operation limit aspect");
        return obj;
    }
}
