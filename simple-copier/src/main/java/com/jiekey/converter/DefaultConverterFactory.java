package com.jiekey.converter;

import com.jiekey.spring.SpringConverterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class DefaultConverterFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConverterFactory.class);
    private static Map<Class, Converter> converterMap = new HashMap<>();
    private static final Object lock = new Object();
    private static Boolean useSpringEnvironment = null;

    public static Converter getConverter(Class clazz) throws IllegalAccessException, InstantiationException {
        boolean debugEnabled = LOGGER.isDebugEnabled();
        if (useSpringEnvironment == null){
            try {
                Class<?> scfClazz = Class.forName("com.jiekey.spring.SpringConverterFactory");
                Method method = scfClazz.getMethod("isSpringEnvironment");
                useSpringEnvironment = (Boolean) method.invoke(null);
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException e){}
        }

        if (useSpringEnvironment) {
            if (debugEnabled) {
                LOGGER.debug("Get '{}' from spring IOC", clazz.getName());
            }
            return SpringConverterFactory.getInstance().getConverter(clazz);
        }
        if (debugEnabled){
            LOGGER.debug("Get '{}' from default container", clazz.getName());
        }

        if (converterMap.containsKey(clazz)){
            return converterMap.get(clazz);
        }
        Converter converter;
        synchronized (lock){
            if (converterMap.containsKey(clazz)) {
                return converterMap.get(clazz);
            }
            converter = (Converter)clazz.newInstance();
            converterMap.put(clazz, converter);
        }
        return converter;
    }

}
