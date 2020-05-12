package com.zone24x7.ibrac.recengine.exceptions;

/**
 * This exception will be thrown when any configuration referred to by RTIS is malformed
 */
public class MalformedConfigurationException extends Exception {
    /**
     * Constructor to create an instance of the MalformedConfigurationException class with a given exception
     * message
     *
     * @param message the exception message
     */
    public MalformedConfigurationException(String message) {
        super(message);
    }

    /**
     * Constructor to create an instance of the MalformedConfigurationException class with a given exception
     * message and the caught underlying exception
     *
     * @param message the exception message
     * @param cause   the underlying exception
     */
    public MalformedConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
