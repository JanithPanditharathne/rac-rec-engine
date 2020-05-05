package com.zone24x7.ibrac.recengine.configuration.fetch;

/**
 * Exception class implementation for the configuration fetching
 */
public class CsConfigurationFetchException extends Exception {

    /**
     * Constructor to instantiate a ConfigurationFetchException.
     */
    public CsConfigurationFetchException() {
        // This is the default constructor. Thus the implementation is empty.
    }

    /**
     * Constructor to instantiate a ConfigurationFetchException with message.
     */
    public CsConfigurationFetchException(String message) {
        super(message);
    }

    /**
     * Constructor to instantiate a ConfigurationFetchException with message and cause.
     */
    public CsConfigurationFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
