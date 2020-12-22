package com.jiekey.converter;

import com.jiekey.core.BeanField;

import java.math.BigDecimal;

public class ToDecimalConverter implements Converter<Object, BigDecimal> {


    @Override
    public BigDecimal convert(Object value, String format, BeanField field) {
        if (value == null) {
            return null;
        }
        if (value instanceof Long || value instanceof Integer) {
            value = BigDecimal.valueOf((Long)value);
        } else if (value instanceof Double || value instanceof Float){
            value = BigDecimal.valueOf((Double)value);
        } else if (value instanceof String){
            value = new BigDecimal((String)value);
        } else {
            throw new NumberFormatException("Not support cast '" + value.getClass().getTypeName() + "' to 'java.math.BigDecimal'");
        }
        return (BigDecimal)value;
    }

    @Override
    public BigDecimal deconvert(Object value, String format, BeanField field) {
        return null;
    }
}
