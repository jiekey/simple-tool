package com.jiekey.core;

import lombok.Data;

@Data
public class AliasAttribute {
    private String value;
    private boolean ignoreNull;
    private NullValue nullValue;
    private String format;
    private Class converter;
}
