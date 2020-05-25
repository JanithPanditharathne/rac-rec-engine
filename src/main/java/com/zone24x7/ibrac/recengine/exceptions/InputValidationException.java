package com.zone24x7.ibrac.recengine.exceptions;

/**
 * Class to handle exceptions related to input validations.
 */
public class InputValidationException extends RuntimeException {

    /**
     * Constructor to instantiate a {@link InputValidationException}.
     */
    public InputValidationException() {
        // This is the default constructor. Thus the implementation is empty.
    }

    /**
     * Constructor to create a {@link InputValidationException} with a message.
     *
     * @param message error message.
     */
    public InputValidationException(String message) {
        super(message);
    }

    /**
     * Constructor to instantiate a RecRequestException with a message and cause.
     *
     * @param message error message.
     * @param cause   cause of the error.
     */
    public InputValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
