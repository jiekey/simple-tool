package com.jiekey.core;

import com.jiekey.converter.Converter;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public class FromMapParser extends AbstractParser {

    public FromMapParser(Object source, boolean matchAllField) {
        super(source, matchAllField);
    }

    @Override
    protected void parseSourceBean() {

    }

    @Override
    protected void parseToBean() {
        parseBean(toClass);
    }

    @Override
    public void to(Object target) throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException, NoSuchFieldException {
        if (target == null) {
            throw new IllegalArgumentException("Target not null");
        } else if (target instanceof Map){
            throw new IllegalArgumentException("Target not is map");
        }
        this.toObj = target;
        this.toClass = target.getClass();
        parseToBean();

        List<BeanField> fields = getFields();
        for (BeanField field : fields) {
            AliasAttribute aliasAttribute = field.getAliasAttribute();

            field.setValue(new FieldInvoker(sourceMap, aliasAttribute.getValue()).get());
            Object value = field.getValue();
            if (value == null) {
                if (aliasAttribute.isIgnoreNull()) {
                    continue;
                }
                if (aliasAttribute.getNullValue() == NullValue.NULL) {
                    field.setValue("");
                }
            }

            Class converterClass = aliasAttribute.getConverter();
            String format = aliasAttribute.getFormat();
            if (format != null || converterClass != null) {
                Converter converter = getConverter(converterClass);
                field.setValue(converter.deconvert(field.getValue(), format, field));
            }

            new FieldInvoker(target, field.getName()).set(field.getValue());
        }
    }
}
