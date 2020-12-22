package com.jiekey.core;

import com.jiekey.converter.DefaultConverter;

import java.lang.annotation.*;

/**
 * @author jiekey
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Alias {

    /**
     * 目标字段名
     */
    String value() default "";

    /**
     * 是否忽略null值转换
     */
    boolean ignoreNull() default true;

    /**
     * null处理结果
     */
    NullValue nullValue() default NullValue.NULL;

    /**
     * 格式化 默认格式化 see @AliasFormat.class 也可自定义格式化与converter配合使用
     */
    String format() default AliasFormat.NULL;

    /**
     * 定义转换的方法 比如
     */
    Class converter() default DefaultConverter.class;

}
