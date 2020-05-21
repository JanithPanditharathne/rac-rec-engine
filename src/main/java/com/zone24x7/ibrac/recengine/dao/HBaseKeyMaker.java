package com.zone24x7.ibrac.recengine.dao;

import java.util.Map;

/**
 * Utility class to get the row key
 */
public class HBaseKeyMaker {

    //Constants
    private static final String ALGORITHM_ID_KEY = "algorithmId";
    private static final String CONSTANT_ALGO_ID = "99";

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
            recommendationKey.setParameter(map.getKey(), map.getValue());
        }
        return recommendationKey.hash();
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
}
