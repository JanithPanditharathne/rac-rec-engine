package com.zone24x7.ibrac.recengine;

import com.zone24x7.ibrac.recengine.util.CorrelationInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import static org.mockito.Mockito.*;

/**
 * Class to test RecWebMvcConfigurer.
 */
public class RecWebMvcConfigurerTest {
    private RecWebMvcConfigurer recWebMvcConfigurer;
    private CorrelationInterceptor correlationInterceptor;

    /**
     * Setup method
     */
    @BeforeEach
    void setUp() {
        correlationInterceptor = mock(CorrelationInterceptor.class);

        recWebMvcConfigurer = new RecWebMvcConfigurer();
        ReflectionTestUtils.setField(recWebMvcConfigurer, "correlationInterceptor", correlationInterceptor);
    }

    /**
     * Test to verify that interceptor is added to the registry correctly.
     */
    @Test
    public void should_add_interceptor_to_the_registry_correctly() {
        InterceptorRegistry interceptorRegistry = mock(InterceptorRegistry.class);
        recWebMvcConfigurer.addInterceptors(interceptorRegistry);
        verify(interceptorRegistry, atMostOnce()).addInterceptor(correlationInterceptor);
    }

    /**
     * Test to verify that cors is added correctly.
     */
    @Test
    public void should_add_cors_correctly() {
        CorsRegistry corsRegistry = mock(CorsRegistry.class);
        CorsRegistration corsRegistration = mock(CorsRegistration.class);

        when(corsRegistry.addMapping(anyString())).thenReturn(corsRegistration);
        when(corsRegistration.allowedOrigins(anyString())).thenReturn(corsRegistration);
        when(corsRegistration.allowedHeaders(anyString())).thenReturn(corsRegistration);
        when(corsRegistration.allowedMethods(anyString())).thenReturn(corsRegistration);

        recWebMvcConfigurer.addCorsMappings(corsRegistry);
        verify(corsRegistry, atMostOnce()).addMapping("/recengine/**");
    }
}