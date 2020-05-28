package com.zone24x7.ibrac.recengine.dao;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
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
    private String resultedHashedKeyForProductNumbers = "-564579472-1035924579";
    private String resultedHashedKeyForNullKeyValue = "-17993255651657001850";

    /**
     * Setup mock classes
     */
    @BeforeEach
    void setUp() {
        testMap = new HashMap<>();
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
}
