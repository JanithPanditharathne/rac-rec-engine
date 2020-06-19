package com.zone24x7.ibrac.recengine.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Class for correlation interceptor.
 */
@Component
public class CorrelationInterceptor extends HandlerInterceptorAdapter {
    private static final String CORRELATION_ID_HEADER_NAME = "X-Correlation-Id";
    private static final String CORRELATION_ID_MDC_ATTRIBUTE_NAME = "correlationId";
    private static final String START_DATE_TIME_MDC_ATTRIBUTE_NAME = "startTime";

    private static final Logger LOGGER = LoggerFactory.getLogger("access");

    /**
     * Method to call before handling the request.
     *
     * @param request  the request
     * @param response the response
     * @param handler  the handler
     * @return true if the request is not handled and false if handled
     * @throws Exception if an error occurs
     */
    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
                             final Object handler) throws Exception {

        final String correlationId = getCorrelationIdFromHeader(request);

        LOGGER.info("[REQUEST] UUID={} METHOD={} PATH={} QSTRING={} ORIGIN={}",
                    correlationId,
                    request.getMethod(),
                    request.getServletPath(),
                    request.getQueryString(),
                    request.getRemoteAddr());

        MDC.put(CORRELATION_ID_MDC_ATTRIBUTE_NAME, correlationId);
        MDC.put(START_DATE_TIME_MDC_ATTRIBUTE_NAME, String.valueOf(System.currentTimeMillis()));

        return true;
    }

    /**
     * Method to call when the request is finished serving.
     *
     * @param request  the request
     * @param response the response
     * @param handler  the handler
     * @param ex       the exception
     * @throws Exception if an error occurs
     */
    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response,
                                final Object handler, final Exception ex) throws Exception {

        final String correlationId = MDC.get(CORRELATION_ID_MDC_ATTRIBUTE_NAME);
        final long startTime = Long.parseLong(MDC.get(START_DATE_TIME_MDC_ATTRIBUTE_NAME));
        long endTime = System.currentTimeMillis();
        long requestTime = endTime - startTime;

        LOGGER.info("[RESPONSE] UUID={} METHOD={} PATH={} QSTRING={} ORIGIN={} STIME={}ms STATUS={}",
                    correlationId,
                    request.getMethod(),
                    request.getServletPath(),
                    request.getQueryString(),
                    request.getRemoteAddr(),
                    requestTime,
                    response.getStatus());

        MDC.remove(CORRELATION_ID_MDC_ATTRIBUTE_NAME);
        MDC.remove(START_DATE_TIME_MDC_ATTRIBUTE_NAME);
    }

    /**
     * Method to get the correlation id from the header and generate if not available.
     *
     * @param request the request
     * @return the correlation id
     */
    private String getCorrelationIdFromHeader(final HttpServletRequest request) {
        String correlationId = request.getHeader(CORRELATION_ID_HEADER_NAME);

        if (StringUtils.isEmpty(correlationId)) {
            correlationId = UUID.randomUUID().toString();
        }

        return correlationId;
    }
}
