package com.zone24x7.ibrac.recengine.pojo.csconfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

/**
 * Class to test BundleAlgorithm.
 */
public class BundleAlgorithmTest {
    private BundleAlgorithm bundleAlgorithm;

    /**
     * Setup method.
     */
    @BeforeEach
    void setUp() {
        bundleAlgorithm = new BundleAlgorithm();
        bundleAlgorithm.setId("1");
        bundleAlgorithm.setName("Algo A");
        bundleAlgorithm.setType("FLAT");
        bundleAlgorithm.setCustomDisplayText("Custom Text");
        bundleAlgorithm.setDefaultDisplayText("Default Text");
    }

    /**
     * Test to verify that attributes set and get are correctly done
     */
    @Test
    public void should_set_and_get_attributes_correctly() {
        assertThat(bundleAlgorithm.getId(), is(equalTo("1")));
        assertThat(bundleAlgorithm.getName(), is(equalTo("Algo A")));
        assertThat(bundleAlgorithm.getType(), is(equalTo("FLAT")));
        assertThat(bundleAlgorithm.getCustomDisplayText(), is(equalTo("Custom Text")));
        assertThat(bundleAlgorithm.getDefaultDisplayText(), is(equalTo("Default Text")));

    }

    /**
     * Test to verify that equal comparison is functioning correctly
     */
    @Test
    public void should_check_equals_comparison_correctly() {
        assertThat(bundleAlgorithm.equals(new Integer(3)), is(equalTo(false)));

        BundleAlgorithm bundleAlgorithm2 = null;
        assertThat(bundleAlgorithm.equals(bundleAlgorithm2), is(equalTo(false)));

        bundleAlgorithm2 = new BundleAlgorithm();
        assertThat(bundleAlgorithm.equals(bundleAlgorithm2), is(equalTo(false)));

        bundleAlgorithm2.setId("1");
        assertThat(bundleAlgorithm.equals(bundleAlgorithm2), is(equalTo(false)));

        bundleAlgorithm2.setName("Algo A");
        assertThat(bundleAlgorithm.equals(bundleAlgorithm2), is(equalTo(false)));

        bundleAlgorithm2.setType("FLAT");
        assertThat(bundleAlgorithm.equals(bundleAlgorithm2), is(equalTo(false)));

        bundleAlgorithm2.setCustomDisplayText("Custom Text");
        assertThat(bundleAlgorithm.equals(bundleAlgorithm2), is(equalTo(false)));

        bundleAlgorithm2.setDefaultDisplayText("Default Text");
        assertThat(bundleAlgorithm.equals(bundleAlgorithm2), is(equalTo(true)));
    }

    /**
     * Test to verify that hash code generation is correctly done
     */
    @Test
    public void should_generate_the_hash_code_correctly() {
        assertThat(bundleAlgorithm.hashCode(), is(equalTo(92877990)));
    }
}