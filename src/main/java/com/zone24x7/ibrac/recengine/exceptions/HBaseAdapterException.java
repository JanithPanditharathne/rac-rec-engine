package com.zone24x7.ibrac.recengine.exceptions;

/**
 * Exception to be thrown when unexpected behaviour occurs in adapter layer.
 */
public class HBaseAdapterException extends Exception {

    /**
     * Constructor to instantiate an HBaseAdapterException with a message string.
     */
    public HBaseAdapterException(String message) {
        super(message);
    }

    /**
     * Constructor to instantiate an HBaseAdapterException with a message and cause.
     *
     * @param message message for the exception.
     * @param cause   cause of the exception.
     */
    public HBaseAdapterException(String message, Throwable cause) {
        super(message, cause);
    }
}
