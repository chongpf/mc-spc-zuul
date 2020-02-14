package com.chong.mcspczuul.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface ApiOperationLimit {

    // 指定限流方法的key值，从application.property文件获取key对应的limitRate
    String value() default "";
}
