package org.lance.cloud.fixture;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;

@Component
public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextHolder.applicationContext = applicationContext;
    }

    /**
     * 当通过接口多个实现时，需要用 beanName区分
     *
     * @param name 注册的 bean名称
     * @param clazz 注册的 bean类型
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        if (applicationContext == null){
            return null;
        }
        return applicationContext.getBean(name, clazz);
    }

    /***
     * 根据一个bean的类型获取配置文件中相应的bean
     */
    public static <T> T getBeanByClass(Class<T> requiredType) {
        if (applicationContext == null) {
            return null;
        }
        return applicationContext.getBean(requiredType);
    }

    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotation){
        return applicationContext.getBeansWithAnnotation(annotation);
    }

    public static ApplicationContext getApplicationContext() {
        return ApplicationContextHolder.applicationContext;
    }
}