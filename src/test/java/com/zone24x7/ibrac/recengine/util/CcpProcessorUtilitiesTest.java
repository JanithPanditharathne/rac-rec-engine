package com.zone24x7.ibrac.recengine.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;

/**
 * Test class of CcpProcessorUtilities.
 */
class CcpProcessorUtilitiesTest {

    /**
     * Method to run before each test.
     */
    @BeforeEach
    public void setup() {
        List<String> whitelistedKeys = new LinkedList<>();
        whitelistedKeys.add("category");

        CcpProcessorUtilities.setWhitelistedCcpKeys(whitelistedKeys);
    }

    /**
     * Test to verify an empty map is returned if the whitelisted ccp key list is empty.
     */
    @Test
    public void should_return_an_empty_filtered_ccp_map_if_whitelisted_ccp_list_is_empty() {
        Map<String, String> ccpMap = new HashMap<>();
        ccpMap.put("department", "clothing");

        Map<String, String> filteredCcpMap = CcpProcessorUtilities.filterChannelContextParamMap(ccpMap);
        assertThat(filteredCcpMap, anEmptyMap());
    }

    /**
     * Test to verify an empty map is returned if input ccp map is empty.
     */
    @Test
    public void should_return_an_empty_filtered_ccp_map_if_input_ccp_is_empty() {
        Map<String, String> filteredCcpMap = CcpProcessorUtilities.filterChannelContextParamMap(Collections.emptyMap());
        assertThat(filteredCcpMap, anEmptyMap());
    }
}