package com.study.demo.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {

    public static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        applicationContext = ac;
    }

    public static<T> T getBean(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }

    public static<T> T getBean(String beanName){
        return (T) applicationContext.getBean(beanName);
    }
}
