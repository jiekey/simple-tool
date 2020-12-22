package com.jiekey.spring;

import com.jiekey.converter.Converter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

@Component
public class SpringConverterFactory implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor {

    private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
    private static final String DEFAULT_PACKAGE = "com.jiekey.converter";

    private static ApplicationContext applicationContext;

    private Set<String> basePackages = new HashSet<>();

    private String scope = ConfigurableBeanFactory.SCOPE_SINGLETON;

    public void setBasePackage(String basePackage) {
        this.basePackages.add(basePackage);
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public static Boolean isSpringEnvironment(){
        if (applicationContext != null){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public static SpringConverterFactory getInstance(){
        return applicationContext.getBean(SpringConverterFactory.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext != null) {
            SpringConverterFactory.applicationContext = applicationContext;
        }
    }

    public Converter getConverter(Class clazz){
        return (Converter)applicationContext.getBean(getBeanSimpleName(clazz));
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        try {
            basePackages.add(DEFAULT_PACKAGE);
            for (String bp : basePackages) {
                String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                        ClassUtils.convertClassNameToResourcePath(new StandardEnvironment().resolveRequiredPlaceholders(bp))
                        + '/' + DEFAULT_RESOURCE_PATTERN;
                ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
                Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
                for (Resource resource : resources) {
                    if (resource.isReadable()){
                        try {
                            MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
                            MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                            Class clazz = Class.forName(metadataReader.getClassMetadata().getClassName());
                            if (matchConverterClass(clazz)){
                                ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader);
                                sbd.setResource(resource);
                                sbd.setSource(resource);
                                Annotation scopeAnnotation;
                                if ((scopeAnnotation = getAnnotation(clazz, Scope.class)) != null) {
                                    sbd.setScope(((Scope)scopeAnnotation).value());
                                } else {
                                    sbd.setScope(scope);
                                }
                                beanDefinitionRegistry.registerBeanDefinition(getBeanSimpleName(clazz), sbd);
                            }
                        } catch (Throwable e){
                            throw new BeanDefinitionStoreException("Failed to read converter class: " + resource, e);
                        }
                    }
                }
            }
        } catch (IOException e){
            throw new BeanDefinitionStoreException("I/O failure during classpath scanning", e);
        }
    }

    private Annotation getAnnotation(Class clazz, Class annotationClass){
        return clazz.getAnnotation(annotationClass);
    }

    private String getBeanSimpleName(Class clazz){
        String beanClassName = clazz.getSimpleName();
        char[] chars = beanClassName.substring(beanClassName.lastIndexOf(".") + 1).toCharArray();
        if (chars[0] >= 'A' && chars[0] <= 'Z') {
            chars[0] += 32;
        }
        return new String(chars);
    }

    private boolean matchConverterClass(Class clazz) {
        if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())
                || !Converter.class.isAssignableFrom(clazz)){
            return false;
        }
        return true;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
