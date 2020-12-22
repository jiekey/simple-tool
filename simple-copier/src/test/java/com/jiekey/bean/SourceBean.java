package com.jiekey.bean;


import com.jiekey.converter.DecimalStringConverter;
import com.jiekey.core.Alias;
import com.jiekey.core.AliasFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SourceBean {

    @Alias("name1")
    private String name;

    private String CS;

    private Integer age;

    @Alias(format = AliasFormat.FIX3, converter = DecimalStringConverter.class)
    private BigDecimal price;

    //@IgnoreField
    @Alias(format = AliasFormat.DATETIME)
    private Date createTime;

}
