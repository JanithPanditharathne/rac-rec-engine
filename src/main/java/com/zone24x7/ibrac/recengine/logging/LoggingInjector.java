package com.zone24x7.ibrac.recengine.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

/**
 * Component to identify the loggers around the application
 */
@Component
public class LoggingInjector implements BeanPostProcessor {

    /**
     * Triggers after initialize the bean with property values
     *
     * @param bean     initialized object bean
     * @param beanName name of the initialized bean
     * @return initialized bean
     * @throws BeansException if fails the post process after initialization
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * Triggers before initialize the bean with property values
     *
     * @param bean initialized object bean
     * @param name name of the bean to be initialized
     * @return bean to be initialized
     * @throws BeansException if fails the process before initialization
     */
    @Override
    public Object postProcessBeforeInitialization(final Object bean, String name) throws BeansException {
        ReflectionUtils.doWithFields(bean.getClass(), field -> {
            // make the field accessible if defined private
            ReflectionUtils.makeAccessible(field);
            if (field.getAnnotation(Log.class) != null) {
                Logger log = LoggerFactory.getLogger(bean.getClass());
                field.set(bean, log);
            }
        });
        return bean;
    }
}
