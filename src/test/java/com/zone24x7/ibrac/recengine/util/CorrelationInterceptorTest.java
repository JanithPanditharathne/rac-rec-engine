package com.zone24x7.ibrac.recengine.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.slf4j.spi.MDCAdapter;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

/**
 * Class to test CorrelationInterceptor.
 */
public class CorrelationInterceptorTest {
    private CorrelationInterceptor correlationInterceptor;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private MDCAdapter mdcAdapter;

    @BeforeEach
    void setUp() throws Exception {
        correlationInterceptor = new CorrelationInterceptor();
        Field loggerField = correlationInterceptor.getClass().getDeclaredField("LOGGER");
        CustomReflectionTestUtils.setFinalStaticField(loggerField, mock(Logger.class));

        request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getServletPath()).thenReturn("/recengine/v1/recommendations");
        when(request.getQueryString()).thenReturn("cid=Web&pgid=Home&plids=Horizontal");
        when(request.getRemoteAddr()).thenReturn("1.0.0.1");
        when(request.getHeader("X-Correlation-Id")).thenReturn(null);

        response = mock(HttpServletResponse.class);
        when(response.getStatus()).thenReturn(200);

        mdcAdapter = mock(MDCAdapter.class);
        ReflectionTestUtils.setField(MDC.class, "mdcAdapter", mdcAdapter);
    }

    /**
     * Test to verify true is returned from pre handler.
     * @throws Exception if an error occurs
     */
    @Test
    public void should_return_true_when_pre_handler_is_success_when_correlation_id_is_not_sent() throws Exception {
        boolean actualReturnValue = correlationInterceptor.preHandle(request, response, new Object());
        Mockito.verify(mdcAdapter, atMostOnce()).put(eq("correlationId"), anyString());
        Mockito.verify(mdcAdapter, atMostOnce()).put(eq("startTime"), anyString());
        assertThat(actualReturnValue, is(equalTo(true)));
    }

    /**
     * Test to verify true is returned from pre handler.
     * @throws Exception if an error occurs
     */
    @Test
    public void should_return_true_when_pre_handler_is_success_when_correlation_id_is_sent() throws Exception {
        when(request.getHeader("X-Correlation-Id")).thenReturn("12345678");
        boolean actualReturnValue = correlationInterceptor.preHandle(request, response, new Object());
        assertThat(actualReturnValue, is(equalTo(true)));
    }

    /**
     * Test to verify that MDC attributes are removed.
     * @throws Exception if an error occurs
     */
    @Test
    public void should_remove_the_mdc_attributes_added_in_pre_handler() throws Exception {
        when(mdcAdapter.get("startTime")).thenReturn(String.valueOf(System.currentTimeMillis()));
        correlationInterceptor.afterCompletion(request, response, new Object(), null);

        Mockito.verify(mdcAdapter, atMostOnce()).remove("correlationId");
        Mockito.verify(mdcAdapter, atMostOnce()).remove("startTime");
    }
}