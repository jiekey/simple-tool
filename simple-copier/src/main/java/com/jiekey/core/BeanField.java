package com.jiekey.core;

import lombok.Data;

@Data
public class BeanField {
    private String name;
    private Class type;
    private Object value;

    private AliasAttribute aliasAttribute;

    public BeanField(){
        this.aliasAttribute = new AliasAttribute();
    }

}
