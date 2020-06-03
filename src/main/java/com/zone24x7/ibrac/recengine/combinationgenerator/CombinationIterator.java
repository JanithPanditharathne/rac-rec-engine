package com.zone24x7.ibrac.recengine.combinationgenerator;

/**
 * Interface of combination iterator
 */
public interface CombinationIterator<T> {

    /**
     * Initializes the combination iterator.
     */
    void initialize();

    /**
     * Checks if a combination exists
     *
     * @return Boolean denoting whether a combination exists
     */
    boolean hasMoreCombinations();

    /**
     * Getter for combination
     *
     * @return Combination map
     */
    T getNextCombination();
}
