package com.jiekey.converter;


import com.jiekey.core.BeanField;

import java.math.BigDecimal;

public class DecimalStringConverter extends DefaultConverter {

    @Override
    public Object convert(Object value, String format, BeanField field) {
        value = super.convert(value, format, field);
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal){
            return ((BigDecimal)value).toPlainString();
        }
        return value;
    }

    @Override
    public Object deconvert(Object value, String format, BeanField field) {
        return super.deconvert(value, format, field);
    }
}
