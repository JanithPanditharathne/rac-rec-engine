package com.zone24x7.ibrac.recengine.combinationgenerator;

import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;

import java.util.Map;

/**
 * Interface of combination iterator provider.
 *
 * @param <T> type of combination iterator.
 */
public interface CombinationIteratorProvider<T> {

    /**
     * Provides a combination iterator for a given algo id and input ccp.
     *
     * @param algoId         Id of the algorithm.
     * @param ccp            Map of ccp params
     * @param recCycleStatus recommendation generation cycle status
     * @return combination iterator
     */
    T getCombinationIterator(String algoId, Map<String, String> ccp, RecCycleStatus recCycleStatus);
}
