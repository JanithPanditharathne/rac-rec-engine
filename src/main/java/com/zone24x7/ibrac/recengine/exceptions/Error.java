package com.zone24x7.ibrac.recengine.exceptions;

/**
 * Class to represent an error.
 */
public class Error {

    private String message;
    private String code;

    /**
     * Constructor to create error instance.
     *
     * @param message error message.
     * @param code    error code.
     */
    public Error(String message, String code) {
        this.message = message;
        this.code = code;
    }

    /**
     * Gets the message.
     *
     * @return message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message.
     *
     * @param message message of the error.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the error code.
     *
     * @return error code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the error code.
     *
     * @param code error code.
     */
    public void setCode(String code) {
        this.code = code;
    }
}
