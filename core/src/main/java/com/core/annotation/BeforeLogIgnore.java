package com.core.annotation;

import java.lang.annotation.*;

/**
 * 忽略返回参数记录日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BeforeLogIgnore {

}
