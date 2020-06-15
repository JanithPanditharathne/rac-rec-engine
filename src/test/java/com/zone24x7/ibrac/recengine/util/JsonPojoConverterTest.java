package com.zone24x7.ibrac.recengine.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.zone24x7.ibrac.recengine.pojo.csconfig.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Test class of JsonPojoConverter
 */
class JsonPojoConverterTest {
    private Rule rule;

    /**
     * Method to run before each test.
     */
    @BeforeEach
    public void setup() {
        rule = new Rule();
        rule.setId("1");
        rule.setGlobal(true);
        rule.setName("Rule1");
        rule.setType("Merchandising");
    }

    /**
     * Test to verify a pojo is converted to a map as expected.
     */
    @Test
    public void should_convert_a_pojo_to_a_map_as_expected() {
        Map<String, String> convertedMap = JsonPojoConverter.toMap(rule);
        assertThat(convertedMap, aMapWithSize(7));
        assertThat(convertedMap.values(), hasItem("Rule1"));
    }

    /**
     * Test to verify a pojo is converted to a json node as expected.
     */
    @Test
    public void should_convert_a_pojo_to_a_json_node_as_expected() {
        JsonNode convertedJsonNode = JsonPojoConverter.toJson(rule);
        assertThat(convertedJsonNode, is(notNullValue()));
    }
}