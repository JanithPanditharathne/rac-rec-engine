package com.zone24x7.ibrac.recengine.dao;

import com.zone24x7.ibrac.recengine.logging.Log;
import org.apache.commons.codec.digest.MurmurHash3;
import org.slf4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class to represent recommendation key.
 */
public class RecommendationKey {

    // TODO move PRODUCT_NUMBERS, PERMUTED_PRODUCT_NUMBERS , SKU_NUMBERS to the config file.
    private static final String PRODUCT_NUMBERS = "productNumbers";
    private static final String PERMUTED_PRODUCT_NUMBERS = "permutedProductNumbers";
    private static final String SKU_NUMBERS = "skuNumbers";
    private static final int PRODUCT_SEED = -889275714;
    private static final int STRING_SEED = -137723950;

    //These keys have multiple values comma separated.
    private List<String> sortedKeys = Arrays.asList(PRODUCT_NUMBERS, SKU_NUMBERS, PERMUTED_PRODUCT_NUMBERS);

    //TreeMap guarantees the alphabetical ordering of the keys
    private Map<String, String> ccpParameters = new TreeMap<>();

    @Log
    private static Logger logger;

    /**
     * Sets a parameter with key and value.
     * Method does not validate the emptiness or nullity of key or value.
     *
     * @param key   parameter key
     * @param value parameter value
     */
    public void setParameter(String key, String value) {
        String tempVal = value;

        if (sortedKeys.contains(key)) {
            List<String> splitList = Arrays.asList(value.split(","));
            Collections.sort(splitList);
            tempVal = splitList.stream().collect(Collectors.joining(","));
        }

        ccpParameters.put(key, cleansValue(tempVal));
    }

    /**
     * Returns the hashed key for the existing values in the map.
     * Murmur hashed with string seed and product seed, and joined them
     *
     * @return hashed key as a String
     */
    public String hash() {
        StringBuilder builder = new StringBuilder();
        ccpParameters.forEach((key, value) -> builder.append(key).append(":").append(value).append(" "));
        String ccpString = builder.toString();

        byte[] ccpByteArray = ccpString.getBytes(StandardCharsets.UTF_8);

        int hashWithStringSeed = MurmurHash3.hash32x86(ccpByteArray, 0, ccpByteArray.length, STRING_SEED);
        int hashWithProductSeed = MurmurHash3.hash32x86(ccpByteArray, 0, ccpByteArray.length, PRODUCT_SEED);

        return Integer.toString(hashWithStringSeed) + Integer.toString(hashWithProductSeed);
    }

    /**
     * Method to lowercase and trim string value.
     *
     * @param value the value to clean
     * @return cleansed value
     */
    private static String cleansValue(String value) {
        if (value == null) {
            return null;
        }

        return value.toLowerCase().trim();
    }
}
