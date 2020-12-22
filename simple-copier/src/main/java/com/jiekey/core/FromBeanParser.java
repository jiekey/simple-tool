package com.jiekey.core;

import com.jiekey.converter.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class FromBeanParser extends AbstractParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(FromBeanParser.class);

    public FromBeanParser(Object source, boolean matchAllField) {
        super(source, matchAllField);
    }

    @Override
    protected void parseSourceBean() {
        parseBean(sourceClass);
    }

    @Override
    protected void parseToBean() {
    }

    @Override
    public void to(Object target) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {
        if (target == null){
            throw new IllegalArgumentException("Target can not is null");
        }
        List<BeanField> fields = getFields();
        for (BeanField field : fields) {
            AliasAttribute aliasAttribute = field.getAliasAttribute();

            field.setValue(new FieldInvoker(sourceObj, field.getName()).get());
            Object value = field.getValue();
            if (value == null){
                if (aliasAttribute.isIgnoreNull()){
                    continue;
                }
                if (aliasAttribute.getNullValue() == NullValue.NULL){
                    field.setValue("");
                }
            }

            Class converterClass = aliasAttribute.getConverter();
            String format = aliasAttribute.getFormat();
            if (format != null || converterClass != null){
                Converter converter = getConverter(converterClass);
                field.setValue(converter.convert(field.getValue(), format, field));
            }

            new FieldInvoker(target, aliasAttribute.getValue()).set(field.getValue());
        }
    }


}
