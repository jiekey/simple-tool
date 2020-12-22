package com.jiekey.core;

import com.jiekey.converter.Converter;
import com.jiekey.converter.DefaultConverterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractParser.class);
    protected Object sourceObj;
    protected Map<String, Object> sourceMap;
    protected Class sourceClass;

    boolean isMap;

    protected Object toObj;
    protected Class toClass;

    protected boolean matchAllField;

    private List<BeanField> fields = new ArrayList<>();

    public List<BeanField> getFields() {
        return this.fields;
    }
    public AbstractParser(Object source, boolean matchAllField) {
        if (source == null){
            throw new IllegalArgumentException("Source can not is null");
        }
        if (source instanceof Map){
            this.sourceMap = (Map<String, Object>) source;
            this.isMap = true;
        } else {
             this.sourceObj = source;
             this.sourceClass = source.getClass();
        }
        this.matchAllField = matchAllField;

        parseSourceBean();
    }

    protected abstract void parseSourceBean();

    protected abstract void parseToBean();

    protected void parseBean(Class clazz){
        Field[] declaredFields = clazz.getDeclaredFields();
        boolean debugEnabled = LOGGER.isDebugEnabled();
        for (Field field : declaredFields) {
            if (field.getAnnotation(IgnoreField.class) == null){
                if(matchAllField){
                    setField(field);
                } else if(!matchAllField && field.getAnnotation(Alias.class) != null){
                    setField(field);
                }
            } else {
                if (debugEnabled){
                    LOGGER.debug("Ignore field '{}'", field.getName());
                }
            }
        }
        LOGGER.info("Completed collect the fields");
    }

    protected void setField(Field field){
        BeanField beanField = new BeanField();

        AliasAttribute aliasAttribute = beanField.getAliasAttribute();
        Alias alias = field.getAnnotation(Alias.class);
        String fieldName = field.getName();
        String targetFieldName = (alias == null || "".equals(alias.value())) ? fieldName : alias.value();
        aliasAttribute.setValue(targetFieldName);
        if (alias != null){
            aliasAttribute.setIgnoreNull(alias.ignoreNull());
            aliasAttribute.setNullValue(alias.nullValue());
            aliasAttribute.setFormat(alias.format());
            aliasAttribute.setConverter(alias.converter());
        }

        beanField.setName(fieldName);
        beanField.setType(field.getType());

        fields.add(beanField);
    }

    protected Converter getConverter(Class clazz) throws InstantiationException, IllegalAccessException {
        return DefaultConverterFactory.getConverter(clazz);
    }

    protected abstract void to(Object target) throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException, NoSuchFieldException;
}
