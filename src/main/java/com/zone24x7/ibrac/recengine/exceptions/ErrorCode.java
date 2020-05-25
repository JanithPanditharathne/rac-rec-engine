package com.zone24x7.ibrac.recengine.exceptions;

/**
 * Enum containing error codes in Rec Engine
 */
public enum ErrorCode {

    RE1000("RE-1000"),
    RE1001("RE-1001"),
    RE1002("RE-1002"),
    RE1003("RE-1003"),
    RE1004("RE-1004"),
    RE1005("RE-1005"),
    RE1006("RE-1006"),
    RE1007("RE-1007");

    private String errorKey;

    /**
     * Constructor of ErrorCode
     *
     * @param errorKey key of the error.
     */
    ErrorCode(final String errorKey) {
        this.errorKey = errorKey;
    }

    /**
     * Overridden toString method to return the key of the error.
     *
     * @return key of the error.
     */
    @Override
    public String toString() {
        return errorKey;
    }
}
