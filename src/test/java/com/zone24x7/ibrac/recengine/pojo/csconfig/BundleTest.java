package com.zone24x7.ibrac.recengine.pojo.csconfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

/**
 * Class to test the bundle class
 */
public class BundleTest {
    private Bundle bundle;
    private List<BundleAlgorithmContainer> algoList;
    private AlgoCombineInfo algoCombineInfo = new AlgoCombineInfo();
    private BundleAlgorithmContainer bundleAlgorithmContainer1 = new BundleAlgorithmContainer();
    private BundleAlgorithmContainer bundleAlgorithmContainer2 = new BundleAlgorithmContainer();

    /**
     * Setup method
     */
    @BeforeEach
    void setUp() {
        bundle = new Bundle();
        bundle.setId("1");
        bundle.setName("Bundle A");
        bundle.setType("Flat");
        bundle.setDefaultLimit(5);
        bundle.setAlgoCombineInfo(algoCombineInfo);
        algoList = new LinkedList<>();
        algoList.add(bundleAlgorithmContainer1);
        algoList.add(bundleAlgorithmContainer2);
        bundle.setAlgorithms(algoList);
    }

    /**
     * Test to verify that attributes set and get are correctly done
     */
    @Test
    public void should_set_and_get_attributes_correctly() {
        assertThat(bundle.getId(), is(equalTo("1")));
        assertThat(bundle.getName(), is(equalTo("Bundle A")));
        assertThat(bundle.getType(), is(equalTo("Flat")));
        assertThat(bundle.getDefaultLimit(), is(equalTo(5)));
        assertThat(bundle.getAlgoCombineInfo(), is(equalTo(algoCombineInfo)));
        assertThat(bundle.getAlgorithms(), is(equalTo(algoList)));
    }

    /**
     * Test to verify that equal comparison is functioning correctly
     */
    @Test
    public void should_check_equals_comparison_correctly() {
        assertThat(bundle.equals(new Integer(3)), is(equalTo(false)));

        Bundle bundle2 = null;
        assertThat(bundle.equals(bundle2), is(equalTo(false)));

        bundle2 = new Bundle();
        assertThat(bundle.equals(bundle2), is(equalTo(false)));

        bundle2.setId("1");
        assertThat(bundle.equals(bundle2), is(equalTo(false)));

        bundle2.setName("Bundle A");
        assertThat(bundle.equals(bundle2), is(equalTo(false)));

        bundle2.setType("Flat");
        assertThat(bundle.equals(bundle2), is(equalTo(false)));

        bundle2.setDefaultLimit(5);
        assertThat(bundle.equals(bundle2), is(equalTo(false)));

        bundle2.setAlgoCombineInfo(algoCombineInfo);
        assertThat(bundle.equals(bundle2), is(equalTo(false)));

        bundle2.setAlgorithms(algoList);
        assertThat(bundle.equals(bundle2), is(equalTo(true)));
    }

    /**
     * Test to verify that hash code generation is correctly done
     */
    @Test
    public void should_generate_the_hash_code_correctly() {
        assertThat(bundle.hashCode(), is(equalTo(1594416669)));
    }
}