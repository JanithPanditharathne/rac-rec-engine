package com.zone24x7.ibrac.recengine.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Properties;

/**
 * Class to handle controller exceptions.
 */
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    @Qualifier("errorProperties")
    private Properties errorProperties;

    /**
     * Handle response error when a {@link InputValidationException} occurs.
     *
     * @param inputValidationException request input validation exception.
     * @return error response.
     */
    @ExceptionHandler(InputValidationException.class)
    public ResponseEntity<ErrorResponse> handleRecRequestException(InputValidationException inputValidationException) {
        return buildRecErrorResponse(inputValidationException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Build error response using the error message.
     *
     * @param error          error.
     * @param httpStatusCode http status code of the error.
     * @return {@link ErrorResponse} instance wrapped in ResponseEntity.
     */
    private ResponseEntity<ErrorResponse> buildRecErrorResponse(String error, HttpStatus httpStatusCode) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.addError(new Error(errorProperties.getProperty(error), error));
        return new ResponseEntity<>(errorResponse, httpStatusCode);
    }
}
