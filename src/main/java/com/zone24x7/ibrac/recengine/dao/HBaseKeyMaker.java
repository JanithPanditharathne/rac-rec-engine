package com.zone24x7.ibrac.recengine.dao;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class to get the row key
 */
public class HBaseKeyMaker {

    //Constants
    private static final String ALGORITHM_ID_KEY = "algorithmId";
    private static final String CONSTANT_ALGO_ID = "99";

    //Params to ignore
    private static List<String> ignoredKeys;
    private static List<String> sortedKeys;

    /**
     * Private constructor to avoid instantiating instances of this final class
     */
    private HBaseKeyMaker() {

    }

    /**
     * Method to get the row key
     *
     * @param algoId Algorithm ID of the recommendation to be retrieved
     * @param params Context parameter list of the recommendation to be retrieved
     * @return A key which can be used to retrieve a recommendation from the recommendation base
     */
    public static String generateRowKey(String algoId, Map<String, String> params) {
        RecommendationKey recommendationKey = new RecommendationKey();

        //First set the algorithm id
        recommendationKey.setParameter(ALGORITHM_ID_KEY, algoId);

        //Now set the other CCPs
        for (Map.Entry<String, String> map : params.entrySet()) {
            String key = map.getKey();
            String value = map.getValue();
            if (!ignoredKeys.contains(key)) {
                if (sortedKeys.contains(key) && StringUtils.isNotEmpty(value) && value.contains(",")) {
                    value = getSortedValue(key);
                }
                recommendationKey.setParameter(key, value);
            }
        }
        return recommendationKey.hash();
    }

    /**
     * Sort the list of comma separated strings.
     *
     * @param commaSeparatedString comma separated string.
     * @return sorted comma separated string.
     */
    private static String getSortedValue(String commaSeparatedString) {
        List<String> splitList = Arrays.asList(commaSeparatedString.split(","));
        Collections.sort(splitList);
        return splitList.stream().collect(Collectors.joining(","));
    }

    /**
     * Method to get the row key hashed from the constant algorithm Id 99
     *
     * @param params Context parameter map to generate the key
     * @return A key which can be used to retrieve a record from HBase
     */
    public static String generateRowKey(Map<String, String> params) {
        return generateRowKey(CONSTANT_ALGO_ID, params);
    }

    /**
     * Setter for sorted keys
     *
     * @param sortedKeys keys to set
     */
    public static void setSortedKeys(List<String> sortedKeys) {
        HBaseKeyMaker.sortedKeys = sortedKeys;
    }

    /**
     * Setter for ignored keys
     *
     * @param ignoredKeys ignored keys
     */
    public static void setIgnoredKeys(List<String> ignoredKeys) {
        HBaseKeyMaker.ignoredKeys = ignoredKeys;
    }
}
