package com.zone24x7.ibrac.recengine.exceptions;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Exception to be thrown for HBase connection related errors.
 *
 */
public class BaseConnectionException extends Exception{

    /**
     * Constructor to instantiate a BaseConnectionException.
     *
     */
    public BaseConnectionException() {
        // This is the default constructor. Thus the implementation is empty.
    }

    /**
     * Constructor to instantiate a BaseConnectionException with message.
     *
     */
    public BaseConnectionException(String message) {
        super(message);
    }

    /**
     * Constructor to instantiate a BaseConnectionException with message and cause.
     *
     */
    public BaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Method to get the minified message.
     *
     * @return the message of the exception and the message cause
     */
    public String getMinifiedMessage() {
        String minifiedMessage = " \n" + this.toString();

        if (ArrayUtils.isNotEmpty(this.getStackTrace()) && this.getStackTrace()[0] != null) {
            minifiedMessage += "\n\tat " + this.getStackTrace()[0].toString();
        }

        if (this.getCause() != null) {
            minifiedMessage += "\n\tCaused by: " + this.getCause().toString();
        }

        return minifiedMessage;
    }

}
