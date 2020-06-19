package com.zone24x7.ibrac.recengine.pojo.csconfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

/**
 * Class to test RegularConfig.
 */
public class RegularConfigTest {
    private RegularConfig regularConfig;

    /**
     * Setup method
     */
    @BeforeEach
    void setUp() {
        regularConfig = new RegularConfig();
        regularConfig.setBundleId("1");
    }

    /**
     * Test to verify that attributes set and get are correctly done
     */
    @Test
    public void should_set_and_get_attributes_correctly() {
        assertThat(regularConfig.getBundleId(), is(equalTo("1")));
    }

    /**
     * Test to verify that equal comparison is functioning correctly
     */
    @Test
    public void should_check_equals_comparison_correctly() {
        assertThat(regularConfig.equals(new Integer(3)), is(equalTo(false)));

        RegularConfig regularConfig2 = null;
        assertThat(regularConfig.equals(regularConfig2), is(equalTo(false)));

        regularConfig2 = new RegularConfig();
        assertThat(regularConfig.equals(regularConfig2), is(equalTo(false)));
        assertThat(regularConfig2.equals(regularConfig), is(equalTo(false)));

        regularConfig2.setBundleId("1111");
        assertThat(regularConfig.equals(regularConfig2), is(equalTo(false)));
        assertThat(regularConfig2.equals(regularConfig), is(equalTo(false)));

        regularConfig2.setBundleId("1");
        assertThat(regularConfig.equals(regularConfig2), is(equalTo(true)));
        assertThat(regularConfig2.equals(regularConfig), is(equalTo(true)));
    }

    /**
     * Test to verify that hash code generation is correctly done
     */
    @Test
    public void should_generate_the_hash_code_correctly() {
        assertThat(regularConfig.hashCode(), is(equalTo(49)));
    }
}