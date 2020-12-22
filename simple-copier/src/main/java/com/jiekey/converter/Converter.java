package com.jiekey.converter;


import com.jiekey.core.BeanField;

/**
 * 转换器，如果是Spring环境则自动托管给Spring管理
 * @param <S>
 * @param <T>
 */
public interface Converter<S, T> {
    T convert(S value, String format, BeanField field);
    T deconvert(S value, String format, BeanField field);
}
