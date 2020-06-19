package com.zone24x7.ibrac.recengine.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Class for rule utilities.
 */
public final class RuleUtils {
    /**
     * Default constructor
     */
    private RuleUtils() {
        // Default constructor made private
    }

    /**
     * Method to convert a value to double.
     * @param value the value
     * @return converted double
     */
    public static Double toDouble(String value) {
        if (StringUtils.isNotEmpty(value)) {
            return Double.valueOf(value);
        }

        return null;
    }

    /**
     * Method to convert a value to integer.
     * @param value the value
     * @return converted integer
     */
    public static Integer toInteger(String value) {
        if (StringUtils.isNotEmpty(value)) {
            return Integer.valueOf(value);
        }

        return null;
    }

    /**
     * Method to convert a value to float.
     * @param value the value
     * @return converted float
     */
    public static Float toFloat(String value) {
        if (StringUtils.isNotEmpty(value)) {
            return Float.valueOf(value);
        }

        return null;
    }

    /**
     * Method to convert a value to boolean.
     * @param value the value
     * @return converted boolean
     */
    public static Boolean toBoolean(String value) {
        if (StringUtils.isNotEmpty(value)) {
            return Boolean.valueOf(value);
        }

        return false;
    }
}
