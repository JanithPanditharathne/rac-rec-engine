package com.zone24x7.ibrac.recengine.rules.exceptions;

/**
 * Exception to be thrown when invalid rule is passed into a generate rule method.
 */
public class InvalidRuleException extends Exception {

    /**
     * Constructor to instantiate an InvalidRuleException with a message string.
     */
    public InvalidRuleException(String message) {
        super(message);
    }

    /**
     * Constructor to instantiate an InvalidRuleException with a message and cause.
     *
     * @param message message for the exception.
     * @param cause   cause of the exception.
     */
    public InvalidRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}
