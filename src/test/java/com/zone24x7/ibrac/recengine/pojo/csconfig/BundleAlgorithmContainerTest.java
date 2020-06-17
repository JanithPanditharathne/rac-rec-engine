package com.zone24x7.ibrac.recengine.pojo.csconfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

/**
 * Class to test BundleAlgorithmContainer.
 */
public class BundleAlgorithmContainerTest {
    private BundleAlgorithmContainer bundleAlgorithmContainer;
    private BundleAlgorithm bundleAlgorithm = new BundleAlgorithm();

    /**
     * Setup method
     */
    @BeforeEach
    void setUp() {
        bundleAlgorithmContainer = new BundleAlgorithmContainer();
        bundleAlgorithmContainer.setRank(1);
        bundleAlgorithmContainer.setAlgorithm(bundleAlgorithm);
    }

    /**
     * Test to verify that attributes set and get are correctly done
     */
    @Test
    public void should_set_and_get_attributes_correctly() {
        assertThat(bundleAlgorithmContainer.getRank(), is(equalTo(1)));
        assertThat(bundleAlgorithmContainer.getAlgorithm(), is(equalTo(bundleAlgorithm)));
    }

    /**
     * Test to verify that equal comparison is functioning correctly
     */
    @Test
    public void should_check_equals_comparison_correctly() {
        assertThat(bundleAlgorithmContainer.equals(new Integer(3)), is(equalTo(false)));

        BundleAlgorithmContainer bundleAlgorithmContainer2 = null;
        assertThat(bundleAlgorithmContainer.equals(bundleAlgorithmContainer2), is(equalTo(false)));

        bundleAlgorithmContainer2 = new BundleAlgorithmContainer();
        assertThat(bundleAlgorithmContainer.equals(bundleAlgorithmContainer2), is(equalTo(false)));

        bundleAlgorithmContainer2.setRank(1);
        assertThat(bundleAlgorithmContainer.equals(bundleAlgorithmContainer2), is(equalTo(false)));

        bundleAlgorithmContainer2.setAlgorithm(bundleAlgorithm);
        assertThat(bundleAlgorithmContainer.equals(bundleAlgorithmContainer2), is(equalTo(true)));
    }

    /**
     * Test to verify that hash code generation is correctly done
     */
    @Test
    public void should_generate_the_hash_code_correctly() {
        assertThat(bundleAlgorithmContainer.hashCode(), is(equalTo(31)));
    }

    /**
     * Test to verify that comparison is correctly done
     */
    @Test
    public void should_compare_with_rank() {
        BundleAlgorithmContainer bundleAlgorithmContainer2 = new BundleAlgorithmContainer();
        bundleAlgorithmContainer2.setRank(5);

        assertThat(bundleAlgorithmContainer.compareTo(bundleAlgorithmContainer2), is(equalTo(-4)));
        assertThat(bundleAlgorithmContainer2.compareTo(bundleAlgorithmContainer), is(equalTo(4)));
    }
}