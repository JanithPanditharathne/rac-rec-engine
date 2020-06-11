package com.zone24x7.ibrac.recengine.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Class for custom reflection utilities
 */
public final class CustomReflectionTestUtils {

    /**
     * Default constructor
     */
    private CustomReflectionTestUtils() {
        // Default constructor made private
    }

    /**
     * Util method to set final static field.
     * @param field the field to set
     * @param newValue the new value to set
     * @throws Exception if an error occurs
     */
    public static void setFinalStaticField(Field field, Object newValue) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, newValue);
    }
}
