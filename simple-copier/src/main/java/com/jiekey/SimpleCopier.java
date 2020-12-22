package com.jiekey;

import com.jiekey.core.FromBeanParser;
import com.jiekey.core.FromMapParser;

import java.util.Map;

public class SimpleCopier {

    public static FromBeanParser fromBean(Object source, boolean matchAllField) {
        return new FromBeanParser(source, matchAllField);
    }

    public static FromMapParser fromMap(Map<String, Object> source, boolean matchAllField) {
        return new FromMapParser(source, matchAllField);
    }

}
