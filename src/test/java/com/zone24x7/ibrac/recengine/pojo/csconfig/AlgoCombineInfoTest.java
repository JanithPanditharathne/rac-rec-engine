package com.zone24x7.ibrac.recengine.pojo.csconfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

/**
 * Class to test AlgoCombineInfo.
 */
public class AlgoCombineInfoTest {
    private AlgoCombineInfo algoCombineInfo;

    /**
     * Setup method
     */
    @BeforeEach
    void setUp() {
        algoCombineInfo = new AlgoCombineInfo();
        algoCombineInfo.setCombineDisplayText("Combined Display Text");
        algoCombineInfo.setEnableCombine(true);
    }

    /**
     * Test to verify that attributes set and get are correctly done
     */
    @Test
    public void should_set_and_get_attributes_correctly() {
        assertThat(algoCombineInfo.getCombineDisplayText(), is(equalTo("Combined Display Text")));
        assertThat(algoCombineInfo.isEnableCombine(), is(equalTo(true)));
    }

    /**
     * Test to verify that equal comparison is functioning correctly
     */
    @Test
    public void should_check_equals_comparison_correctly() {
        assertThat(algoCombineInfo.equals(new Integer(3)), is(equalTo(false)));

        AlgoCombineInfo algoCombineInfo2 = null;
        assertThat(algoCombineInfo.equals(algoCombineInfo2), is(equalTo(false)));

        algoCombineInfo2 = new AlgoCombineInfo();
        assertThat(algoCombineInfo.equals(algoCombineInfo2), is(equalTo(false)));

        algoCombineInfo2.setEnableCombine(true);
        assertThat(algoCombineInfo.equals(algoCombineInfo2), is(equalTo(false)));

        algoCombineInfo2.setCombineDisplayText("Combined Display Text");
        assertThat(algoCombineInfo.equals(algoCombineInfo2), is(equalTo(true)));
    }

    /**
     * Test to verify that hash code generation is correctly done
     */
    @Test
    public void should_generate_the_hash_code_correctly() {
        assertThat(algoCombineInfo.hashCode(), is(equalTo(1947358021)));
    }
}