package com.jiekey.converter;


import com.jiekey.core.BeanField;

public class ToStringConverter implements Converter {


    @Override
    public Object convert(Object value, String format, BeanField field) {
        if (value != null) {
            return value.toString();
        }
        return null;
    }

    @Override
    public Object deconvert(Object value, String format, BeanField field) {
        return null;
    }
}
