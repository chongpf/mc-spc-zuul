package com.chong.mcspczuul.aspect;

import com.chong.common.entity.ResponseData;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Aspect
public class ControllerTraceAspect {
    private final static Logger logger = LoggerFactory.getLogger(ControllerTraceAspect.class);
    private String message1 = "@Before is run.";
    private String message21 = "@Arroud is run before.";
    private String message22 = "@Arroud is run after.";
    private String message3 = "@AfterReturning is run.";
    private String message4 = "@AfterThrowing is run.";
    private String message5 = "@After is run.";

    @Pointcut("execution(* com.chong.mcspczuul.controller.*.*(..)) "+
    "&& !execution(* com.chong.mcspczuul.controller.ErrorHandlerController.*(..)) ")
    private void pointCut(){}

    @Before(value ="pointCut()")
    public void before(JoinPoint joinPoint){
        logger.info(message1);
        logger.info("signature:"+joinPoint.getSignature());
        logger.info("args:"+ Arrays.asList(joinPoint.getArgs()));
    }

    @Around("pointCut()")
    public Object aroud(ProceedingJoinPoint joinPoint){
        Object obj = null;
        try {
            logger.info(message21);
            logger.info("signature:"+joinPoint.getSignature());
            logger.info("args:"+ Arrays.asList(joinPoint.getArgs()));
            obj = joinPoint.proceed();
            logger.info(message22);
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
        return obj;
    }
    @After(value = "pointCut()")
    public void after(JoinPoint joinPoint){
        logger.info(message5);
        logger.info("signature:"+joinPoint.getSignature());
        logger.info("args:"+ Arrays.asList(joinPoint.getArgs()));
    }

    @AfterReturning(value = "pointCut()",returning = "responseData")
    public void afterReturning(JoinPoint joinPoint, ResponseData responseData){
        logger.info(message3);
        logger.info("signature:"+joinPoint.getSignature());
        logger.info("args:"+ Arrays.asList(joinPoint.getArgs()));
    }

    @AfterThrowing(value = "pointCut()",throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex){
        logger.info(message4);
        logger.info("signature:"+joinPoint.getSignature());
        logger.info("args:"+ Arrays.asList(joinPoint.getArgs()));
        logger.info(ex.getMessage());
    }

    // 以下是对带参数的方法进行过滤时的做法.
//    @Pointcut("execution(* com.chong.mcspczuul.controller.TimeController.*(String))"
//            +"&& args(param)")
//    private void pointCut(String param){}
//    @Before("pointCut(param)")
//    public void before(String param){
//        logger.info(message1+DateUtil.getDateTime());
//        logger.info(param);
//    }
//
//    @Around("pointCut(param)")
//    public Object aroud(ProceedingJoinPoint proceedingJoinPoint,String param){
//        Object obj = null;
//        try {
//            logger.info(message21 + DateUtil.getDateTime());
//            obj = proceedingJoinPoint.proceed();
//            logger.info(message22 + DateUtil.getDateTime());
//        }catch (Throwable throwable){
//            throwable.printStackTrace();
//        }
//        return obj;
//    }
//
//    @AfterReturning("pointCut(param)")
//    public void afterReturning(String param){
//        logger.info(message3+DateUtil.getDateTime());
//        logger.info(param);
//    }
}
