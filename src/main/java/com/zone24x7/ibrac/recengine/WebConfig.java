package com.zone24x7.ibrac.recengine;

import com.zone24x7.ibrac.recengine.util.CorrelationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Class to represent the web mvc configurer.
 */
@Component
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private CorrelationInterceptor correlationInterceptor;

    /**
     * Method to add interceptors
     *
     * @param registry the interceptor registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(correlationInterceptor);
    }
}
