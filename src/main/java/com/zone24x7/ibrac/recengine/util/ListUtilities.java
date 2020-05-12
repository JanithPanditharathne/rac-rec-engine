package com.zone24x7.ibrac.recengine.util;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Class to list utilities.
 */
public final class ListUtilities {

    /**
     * Default private constructor
     */
    private ListUtilities() {
        // Private constructor
    }

    /**
     * Method to remove the duplicates in a list.
     *
     * @param list the list to remove duplicates
     * @param <I>  the type of list
     * @return the duplicate removed list
     */
    public static <I> List<I> removeDuplicates(List<I> list) {
        return new LinkedList<>(new LinkedHashSet<>(list));
    }
}
