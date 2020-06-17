package com.zone24x7.ibrac.recengine.pojo.csconfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

/**
 * Class to test TestConfig.
 */
public class TestConfigTest {
    private TestConfig testConfig;

    /**
     * Setup method
     */
    @BeforeEach
    void setUp() {
        testConfig = new TestConfig();
        testConfig.setId("1");
    }

    /**
     * Test to verify that attributes set and get are correctly done
     */
    @Test
    public void should_set_and_get_attributes_correctly() {
        assertThat(testConfig.getId(), is(equalTo("1")));
    }

    /**
     * Test to verify that equal comparison is functioning correctly
     */
    @Test
    public void should_check_equals_comparison_correctly() {
        assertThat(testConfig.equals(new Integer(3)), is(equalTo(false)));

        TestConfig testConfig2 = null;
        assertThat(testConfig.equals(testConfig2), is(equalTo(false)));

        testConfig2 = new TestConfig();
        assertThat(testConfig.equals(testConfig2), is(equalTo(false)));

        testConfig2.setId("1");
        assertThat(testConfig.equals(testConfig2), is(equalTo(true)));
    }

    /**
     * Test to verify that hash code generation is correctly done
     */
    @Test
    public void should_generate_the_hash_code_correctly() {
        assertThat(testConfig.hashCode(), is(equalTo(49)));
    }
}