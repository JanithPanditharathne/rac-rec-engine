package com.zone24x7.ibrac.recengine.pojo.rules;

import java.util.Map;

/**
 * POJO class for Matching Conditions
 */
public class MatchingCondition {
    private Map<String, String> matchingMap;

    /**
     * Sets the matching condition map
     *
     * @param matchingMap the matching condition map
     */
    public void setMatchingMap(Map<String, String> matchingMap) {
        this.matchingMap = matchingMap;
    }

    /**
     * Gets the matching conditions map
     *
     * @return the matching condition map
     */
    public Map<String, String> getMatchingMap() {
        return matchingMap;
    }
}
