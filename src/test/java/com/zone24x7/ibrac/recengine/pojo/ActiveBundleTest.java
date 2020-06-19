package com.zone24x7.ibrac.recengine.pojo;

import com.zone24x7.ibrac.recengine.pojo.csconfig.AlgoCombineInfo;
import com.zone24x7.ibrac.recengine.pojo.csconfig.BundleAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

/**
 * Class to test ActiveBundle
 */
public class ActiveBundleTest {
    private ActiveBundle activeBundle;
    private AlgoCombineInfo algoCombineInfo;
    private List<BundleAlgorithm> bundleAlgorithmList = new LinkedList<>();
    private Set<String> placementFilteringRules = new LinkedHashSet<>();

    /**
     * Setup method
     */
    @BeforeEach
    void setUp() {
        algoCombineInfo = new AlgoCombineInfo();
        algoCombineInfo.setCombineDisplayText("Combined Display Text");
        algoCombineInfo.setEnableCombine(true);
        activeBundle = new ActiveBundle(new ActiveBundle.BundleInfo("1", "A", "Flat"), "Rec 1", 8, bundleAlgorithmList, algoCombineInfo, placementFilteringRules);
    }

    /**
     * Test to verify that attributes set and get are correctly done
     */
    @Test
    public void should_set_and_get_attributes_correctly() {
        assertThat(activeBundle.getId(), is(equalTo("1")));
        assertThat(activeBundle.getName(), is(equalTo("A")));
        assertThat(activeBundle.getType(), is(equalTo("Flat")));
        assertThat(activeBundle.getRecId(), is(equalTo("Rec 1")));
        assertThat(activeBundle.getLimitToApply(), is(equalTo(8)));
        assertThat(activeBundle.getValidAlgorithmListToExecute(), is(equalTo(bundleAlgorithmList)));
        assertThat(activeBundle.getAlgoCombineInfo(), is(equalTo(algoCombineInfo)));
        assertThat(activeBundle.getPlacementFilteringRuleIds(), is(equalTo(placementFilteringRules)));

    }

    /**
     * Test to verify that equal comparison is functioning correctly
     */
    @Test
    public void should_check_equals_comparison_correctly() {
        ActiveBundle activeBundle2 = new ActiveBundle(new ActiveBundle.BundleInfo("1", "A", "Flat"), "Rec 1", 8, bundleAlgorithmList, algoCombineInfo, placementFilteringRules);
        assertThat(activeBundle.equals(activeBundle2), is(equalTo(true)));
    }

    /**
     * Test to verify that hash code generation is correctly done
     */
    @Test
    public void should_generate_the_hash_code_correctly() {
        assertThat(activeBundle.hashCode(), is(equalTo(-843206033)));
    }
}