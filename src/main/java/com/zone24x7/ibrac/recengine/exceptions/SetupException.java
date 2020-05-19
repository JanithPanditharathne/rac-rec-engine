package com.zone24x7.ibrac.recengine.exceptions;

/**
 * This exception will be thrown when any component of RTIS detects that it has not been properly setup to provide
 * it's correct functionality
 */
public class SetupException extends Exception {
    /**
     * Default constructor of the SetupException class
     */
    public SetupException() {
        //This is the default constructor, thus the implementation is empty
    }

    /**
     * Constructor to create an instance of the SetupException class with a given exception message
     *
     * @param message the exception message
     */
    public SetupException(String message) {
        super(message);
    }

    /**
     * Constructor to create an instance of the SetupException class with a given exception
     * message and the caught underlying exception
     *
     * @param message the exception message
     * @param cause   the underlying exception
     */
    public SetupException(String message, Throwable cause) {
        super(message, cause);
    }
}
