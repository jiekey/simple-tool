package com.jiekey.converter;


import com.jiekey.core.AliasFormat;
import com.jiekey.core.AliasAttribute;
import com.jiekey.core.BeanField;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DefaultConverter implements Converter {

    @Override
    public Object convert(Object value, String format, BeanField field) {
        AliasAttribute aliasAttribute = field.getAliasAttribute();
        if (value == null || aliasAttribute.getFormat() == null) {
            return null;
        }
        if (field.getType() == Date.class){
            Date dateValue = (Date)value;
            SimpleDateFormat sdf;
            switch (format) {
                case AliasFormat.DATETIME:
                    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    value = sdf.format(dateValue);
                    break;
                case AliasFormat.DATE:
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                    value = sdf.format(dateValue);
                    break;
                case AliasFormat.TIME:
                    sdf = new SimpleDateFormat("HH:mm:ss");
                    value = sdf.format(dateValue);
                    break;
                case AliasFormat.TIMESTAMP:
                    value = dateValue.getTime();
                    break;
                default:
            }
        }
        if (field.getType() == BigDecimal.class){
            BigDecimal decimalValue = (BigDecimal)value;
            switch (format) {
                case AliasFormat.INT:
                    value = decimalValue.setScale(0, RoundingMode.HALF_UP);
                    break;
                case AliasFormat.FLOAT:
                    value = decimalValue.floatValue();
                    break;
                case AliasFormat.DOUBLE:
                    value = decimalValue.doubleValue();
                    break;
                case AliasFormat.FIX1:
                    value = decimalValue.setScale(1, RoundingMode.HALF_UP);
                    break;
                case AliasFormat.FIX2:
                    value = decimalValue.setScale(2, RoundingMode.HALF_UP);
                    break;
                case AliasFormat.FIX3:
                    value = decimalValue.setScale(3, RoundingMode.HALF_UP);
                    break;
                case AliasFormat.FIX4:
                    value = decimalValue.setScale(4, RoundingMode.HALF_UP);
                    break;
                default:
            }
        }
        return value;
    }

    @Override
    public Object deconvert(Object value, String format, BeanField field) {
        AliasAttribute aliasAttribute = field.getAliasAttribute();
        if (value == null || aliasAttribute.getFormat() == null) {
            return null;
        }
        Class type = field.getType();
        if (type == Date.class){
            Calendar calendar = Calendar.getInstance();
            switch (format) {
                case AliasFormat.DATETIME:
                    String[] split = ((String) value).split(" ");
                    String[] date = split[0].split("-");
                    String[] time = split[1].split(":");
                    calendar.set(Integer.valueOf(date[0]), Integer.valueOf(date[1]), Integer.valueOf(date[2]),
                            Integer.valueOf(time[0]), Integer.valueOf(time[1]), Integer.valueOf(time[2]));
                    value = calendar.getTime();
                    break;
                case AliasFormat.DATE:
                    String[] date1 = ((String) value).split("-");
                    calendar.set(Integer.valueOf(date1[0]), Integer.valueOf(date1[1]), Integer.valueOf(date1[2]));
                    value = calendar.getTime();
                    break;
                case AliasFormat.TIME:
                    String[] time1 = ((String) value).split(":");
                    calendar.set(0, 0, 0, Integer.valueOf(time1[0]), Integer.valueOf(time1[1]), Integer.valueOf(time1[2]));
                    value = calendar.getTime();
                    break;
                case AliasFormat.TIMESTAMP:
                    value = new Date((Long)value);
                    break;
                default:
            }
        }
        if (type == BigDecimal.class){
            switch (format) {
                case AliasFormat.INT:
                    value = BigDecimal.valueOf((Integer)value);
                    break;
                case AliasFormat.FLOAT:
                    value = BigDecimal.valueOf((Float)value);
                    break;
                case AliasFormat.DOUBLE:
                    value = BigDecimal.valueOf((Double) value);
                    break;
                case AliasFormat.FIX1:
                    value = new BigDecimal((String) value).setScale(1, RoundingMode.HALF_UP);
                    break;
                case AliasFormat.FIX2:
                    value = new BigDecimal((String) value).setScale(2, RoundingMode.HALF_UP);
                    break;
                case AliasFormat.FIX3:
                    value = new BigDecimal((String) value).setScale(3, RoundingMode.HALF_UP);
                    break;
                case AliasFormat.FIX4:
                    value = new BigDecimal((String) value).setScale(4, RoundingMode.HALF_UP);
                    break;
                default:
            }
        }
        return value;
    }
}
