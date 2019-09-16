package com.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel annotation define
 *
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {

    /**
     * export field name(Call the get method of the current field)
     */
    String value() default "";

    /**
     * export field title
     */
    String title();

    /**
     * field type(0:export/import 1:export only 2:import only)
     */
    int type() default 0;

    /**
     * export field alignment(0:auto 1:left 2;center 3:right)
     */
    int align() default 0;

    /**
     * export field sort(asc)
     */
    int sort() default 0;

    /**
     * If it is a dictionary type, set the type value of the dictionary
     */
    String dictType() default "";

    /**
     * reflection type
     */
    Class<?> fieldType() default Class.class;

    /**
     * Field ownership group (export imports based on grouping)
     */
    int[] groups() default {};
}
