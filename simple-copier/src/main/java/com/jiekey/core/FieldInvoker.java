package com.jiekey.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class FieldInvoker {
    private static final Logger LOGGER = LoggerFactory.getLogger(FieldInvoker.class);
    protected Object obj;
    protected String fieldName;
    protected boolean isMap;

    public FieldInvoker(Object obj, String fieldName){
        this.obj = obj;
        this.fieldName = fieldName;
        if (obj instanceof Map){
            this.isMap = true;
        }
    }

    protected String getMethodName(String prefix, String fieldName){
        char[] chars = fieldName.toCharArray();
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] -= 32;
        }
        return prefix + new String(chars);
    }

    public Object get() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if (isMap){
            return ((Map<String, Object>)obj).get(this.fieldName);
        } else {
            try {
                Method method = obj.getClass().getMethod(getMethodName("get", fieldName));
                Object val = method.invoke(obj);
                if (LOGGER.isDebugEnabled()){
                    LOGGER.debug("Get the value of field '{}' is '{}'", fieldName, val);
                }
                return val;
            } catch (Exception e){
                LOGGER.error("Error when calling the getter method of field '{}'", fieldName);
                throw e;
            }
        }
    }
    public Object set(Object value) throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (isMap){
            return ((Map<String, Object>)obj).put(this.fieldName, value);
        } else {
            try {
                Class<?> type = obj.getClass().getDeclaredField(fieldName).getType();
                Method method = obj.getClass().getMethod(getMethodName("set", fieldName), type);
                Object val = method.invoke(obj, value);
                if (LOGGER.isDebugEnabled()){
                    LOGGER.debug("Set the value of field '{}' to '{}'", fieldName, val);
                }
                return val;
            }catch (NoSuchFieldException e){
                LOGGER.error("Error getting the type of field '{}'", fieldName);
                throw e;
            } catch (NoSuchMethodException e){
                LOGGER.error("Error getting the method of field '{}'", fieldName);
                throw e;
            } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e){
                LOGGER.error("Error when setting '{}' of field '{}'", value, fieldName);
                throw e;
            }
        }
    }

}
