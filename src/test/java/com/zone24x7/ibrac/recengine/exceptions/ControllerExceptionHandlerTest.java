package com.zone24x7.ibrac.recengine.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Properties;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Test class for ControllerExceptionHandler
 */
class ControllerExceptionHandlerTest {
    private ControllerExceptionHandler controllerExceptionHandler;

    @Mock
    private Properties errorProperties;

    /**
     * Setup method to prepare the mocked objects before running the tests.
     */
    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        controllerExceptionHandler = new ControllerExceptionHandler();

        ReflectionTestUtils.setField(controllerExceptionHandler, "errorProperties", errorProperties);
        when(errorProperties.getProperty("1005")).thenReturn("validation exception occurred");
    }

    /**
     * Test to verify error response is returned as expected when validation error is occurred.
     */
    @Test
    public void should_handle_validation_exception_and_return_bad_request_error_response_as_expected() {
        InputValidationException inputValidationException = new InputValidationException("1005", new Throwable());

        ResponseEntity<ErrorResponse> responseEntity = controllerExceptionHandler.handleRecRequestException(inputValidationException);
        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(responseEntity.getBody().getErrors().get(0).getCode(), equalTo("1005"));
        assertThat(responseEntity.getBody().getErrors().get(0).getMessage(), equalTo("validation exception occurred"));
    }
}