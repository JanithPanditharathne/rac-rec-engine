package com.zone24x7.ibrac.recengine.util;

import java.util.regex.Pattern;

/**
 * Validation utilities
 */
public class ValidationUtilities {
    /**
     * Return whether the input matches the pattern
     *
     * @param pattern regex pattern
     * @param input   string to match
     * @return returns true if matches
     */
    public static boolean matchesPattern(Pattern pattern, String input) {
        return pattern.matcher(input).matches();
    }
}
