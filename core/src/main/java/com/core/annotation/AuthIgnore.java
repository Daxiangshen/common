package com.core.annotation;

import java.lang.annotation.*;

/**
 * 忽略登录认证校验 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthIgnore {

}
