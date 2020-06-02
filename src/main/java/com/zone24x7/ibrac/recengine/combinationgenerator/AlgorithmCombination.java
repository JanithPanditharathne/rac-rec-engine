package com.zone24x7.ibrac.recengine.combinationgenerator;

import java.util.Map;

/**
 * Class for representing an algorithm combination
 */
public class AlgorithmCombination {
    private final Map<String, String> combinationMap;

    /**
     * Constructor
     *
     * @param combinationMap Map containing CCP params for a particular combination
     */
    public AlgorithmCombination(Map<String, String> combinationMap) {
        this.combinationMap = combinationMap;
    }

    /**
     * Getter for combination map
     *
     * @return Combination map
     */
    public Map<String, String> getCombinationMap() {
        return combinationMap;
    }
}
