package com.zone24x7.ibrac.recengine.pojo.csconfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

/**
 * Class to test BundleConfig.
 */
public class BundleConfigTest {
    private BundleConfig bundleConfig;
    private List<Bundle> bundleList = new LinkedList<>();
    private Bundle bundle1 = new Bundle();
    private Bundle bundle2 = new Bundle();

    /**
     * Setup method
     */
    @BeforeEach
    void setUp() {
        bundleConfig = new BundleConfig();
        bundleList.add(bundle1);
        bundleList.add(bundle2);
        bundleConfig.setBundles(bundleList);
    }

    /**
     * Test to verify that attributes set and get are correctly done
     */
    @Test
    public void should_set_and_get_attributes_correctly() {
        assertThat(bundleConfig.getBundles(), is(equalTo(bundleList)));
    }

    /**
     * Test to verify that equal comparison is functioning correctly
     */
    @Test
    public void should_check_equals_comparison_correctly() {
        assertThat(bundleConfig.equals(new Integer(3)), is(equalTo(false)));

        BundleConfig bundleConfig2 = null;
        assertThat(bundleConfig.equals(bundleConfig2), is(equalTo(false)));

        bundleConfig2 = new BundleConfig();
        assertThat(bundleConfig.equals(bundleConfig2), is(equalTo(false)));

        bundleConfig2.setBundles(bundleList);
        assertThat(bundleConfig.equals(bundleConfig2), is(equalTo(true)));
    }

    /**
     * Test to verify that hash code generation is correctly done
     */
    @Test
    public void should_generate_the_hash_code_correctly() {
        assertThat(bundleConfig.hashCode(), is(equalTo(961)));
    }
}