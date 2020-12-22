package com.jiekey.converter;


import com.jiekey.core.BeanField;

import java.math.BigDecimal;

public class StringDecimalConverter implements Converter<String, BigDecimal> {

    @Override
    public BigDecimal convert(String value, String format, BeanField field) {
        if (value == null || "".equals(value)) {
            return null;
        }
        return new BigDecimal(value);
    }

    @Override
    public BigDecimal deconvert(String value, String format, BeanField field) {
        return null;
    }
}
