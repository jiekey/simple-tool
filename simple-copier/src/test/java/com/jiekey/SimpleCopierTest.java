package com.jiekey;

import com.jiekey.bean.SourceBean;
import com.jiekey.bean.TargetBean;
import com.jiekey.spring.SpringConverterFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SimpleCopierTest {
    @Bean
    public SpringConverterFactory springConverterFactory(){
        SpringConverterFactory springConverterFactory = new SpringConverterFactory();
        springConverterFactory.setBasePackage("com.jiekey");
        springConverterFactory.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
        return springConverterFactory;
    }

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        boolean startSpring = true;
        AnnotationConfigApplicationContext app;
        if (startSpring){
            app = new AnnotationConfigApplicationContext("com.jiekey.spring");
            app.start();
        }

        SourceBean sourceBean = new SourceBean();
        sourceBean.setName("xiaoming");
        sourceBean .setCS("你好 哈哈哈");
        sourceBean .setAge(20);
        sourceBean   .setPrice(BigDecimal.TEN);
        sourceBean  .setCreateTime(new Date());
        TargetBean targetBean = new TargetBean();
        Map<String, Object> targetMap = new HashMap<>();
        SourceBean targetBean1 = new SourceBean();
        //第一遍有类加载耗时
        boolean ignoreFirst = false;
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            SimpleCopier.fromBean(sourceBean, true).to(targetMap);
            targetMap.put("price", "23.2345342");
            SimpleCopier.fromMap(targetMap, true).to(targetBean1);

            /*targetBean.setName1(sourceBean.getName1());
            targetBean.setCS(sourceBean.getCS());
            targetBean.setAge(sourceBean.getAge());
            targetBean.setPrice(BigDecimal.TEN.toString());*/

            if (ignoreFirst && i == 0){
                start = System.currentTimeMillis();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("cost:" + (end - start));
        System.out.println(targetMap);
        System.out.println(targetBean1.toString());
    }
}
