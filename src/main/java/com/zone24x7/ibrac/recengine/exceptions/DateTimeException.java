package com.zone24x7.ibrac.recengine.exceptions;

/**
 * Exception to be thrown when an error occurs while converting to zoned datetime.
 */
public class DateTimeException extends RuntimeException {

    /**
     * Constructor to instantiate an instance of DateTimeException with message.
     *
     * @param message Message of the exception.
     */
    public DateTimeException(String message) {
        super(message);
    }

    /**
     * Constructor to instantiate an instance of DateTimeException with message and cause.
     *
     * @param message Message of the exception.
     * @param cause   Cause of the exception.
     */
    public DateTimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
