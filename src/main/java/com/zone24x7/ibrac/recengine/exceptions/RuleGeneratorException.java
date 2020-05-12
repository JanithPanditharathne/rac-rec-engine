package com.zone24x7.ibrac.recengine.exceptions;

/**
 * Exception to be thrown when an error occurs while generating rules.
 */
public class RuleGeneratorException extends Exception {

    /**
     * Constructor to instantiate an instance of RuleGeneratorException with message.
     *
     * @param message Message of the exception.
     */
    public RuleGeneratorException(String message) {
        super(message);
    }

    /**
     * Constructor to instantiate an instance of RuleGeneratorException with message and cause.
     *
     * @param message Message of the exception.
     * @param cause   Cause of the exception.
     */
    public RuleGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }
}
