package com.zone24x7.ibrac.recengine.dao;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Class to test HBaseKeyMaker.
 */
@Disabled
public class HBaseKeyMakerTest {

    private Map<String, String> testMap;
    private String algorithmID = "100";
    private String resultedHashedKey = "-1314037956-1973208397";
    private String resultedHashedKeyWithNoAlgoId = "-2969697551934272923";
    private String resultedHashedKeyForMultivaluedKeys = "6568817321438813280";
    private String resultedHashedKeyForNullKeyValue = "-17993255651657001850";

    private static List<String> ignoredKeys;
    private static List<String> sortedKeys;

    /**
     * Setup mock classes
     */
    @BeforeEach
    void setUp() {
        testMap = new HashMap<>();

        // Fill the ignored keys list
        fill_ignored_keys_array();

        // Fill the sorted keys list
        fill_sorted_keys_array();

        // Set the lists to the HBaseKeyMaker.
        HBaseKeyMaker.setIgnoredKeys(ignoredKeys);
        HBaseKeyMaker.setSortedKeys(sortedKeys);
    }

    /**
     * Test to verify the row key.
     */
    @Test
    public void should_return_correct_key() {
        // Put values to the map.
        testMap.put("department", "Clothing");
        String result = HBaseKeyMaker.generateRowKey(algorithmID, testMap);
        assertThat(result, is(equalTo(resultedHashedKey)));
    }

    /**
     * Test to verify the row key when null passed as the value.
     */
    @Test
    public void should_return_correct_key_when_null_passed_as_value() {
        // Put values to the map.
        testMap.put("department", null);
        String result = HBaseKeyMaker.generateRowKey(algorithmID, testMap);
        assertEquals(resultedHashedKeyForNullKeyValue, result);
    }

    /**
     * Test to verify the row key generateRowKey without algorithm id provided.
     */
    @Test
    public void should_return_correct_key_without_algorithm_id() {
        // Put values to the map.
        testMap.put("department", "Clothing");
        String result = HBaseKeyMaker.generateRowKey(testMap);
        assertEquals(resultedHashedKeyWithNoAlgoId, result);
    }

    /**
     * Test to verify the row key generateRowKey without algorithm id provided.
     */
    @Test
    public void should_return_correct_key_for_multi_valued_keys() {
        // Put values to the map.
        testMap.put("productNumbers", "1234,34565");
        String result = HBaseKeyMaker.generateRowKey(testMap);
        assertEquals(resultedHashedKeyForMultivaluedKeys, result);
    }

    /**
     * Fill the ignoredKeys array list
     */
    private void fill_ignored_keys_array(){

        ignoredKeys = new ArrayList<>();

        ignoredKeys.add("shipNodes");
        ignoredKeys.add("shipNode");
    }

    /**
     * Fill the sortedKeys array list
     */
    private void fill_sorted_keys_array(){

        sortedKeys = new ArrayList<>();

        sortedKeys.add("productNumbers");
        sortedKeys.add("permutedProductNumbers");
        sortedKeys.add("skuNumbers");
    }
}
