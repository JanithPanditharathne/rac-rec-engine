package com.zone24x7.ibrac.recengine.pojo.csconfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

/**
 * Class to test RecConfig.
 */
public class RecConfigTest {
    private RecConfig recConfig;
    private List<Rec> recList = new LinkedList<>();
    private Rec rec1 = new Rec();
    private Rec rec2 = new Rec();

    /**
     * Setup method
     */
    @BeforeEach
    void setUp() {
        recConfig = new RecConfig();
        recList.add(rec1);
        recList.add(rec2);
        recConfig.setRecs(recList);
    }

    /**
     * Test to verify that attributes set and get are correctly done
     */
    @Test
    public void should_set_and_get_attributes_correctly() {
        assertThat(recConfig.getRecs(), is(equalTo(recList)));
    }

    /**
     * Test to verify that equal comparison is functioning correctly
     */
    @Test
    public void should_check_equals_comparison_correctly() {
        assertThat(recConfig.equals(new Integer(3)), is(equalTo(false)));

        RecConfig recConfig2 = null;
        assertThat(recConfig.equals(recConfig2), is(equalTo(false)));

        recConfig2 = new RecConfig();
        assertThat(recConfig.equals(recConfig2), is(equalTo(false)));

        recConfig2.setRecs(recList);
        assertThat(recConfig.equals(recConfig2), is(equalTo(true)));
    }

    /**
     * Test to verify that hash code generation is correctly done
     */
    @Test
    public void should_generate_the_hash_code_correctly() {
        assertThat(recConfig.hashCode(), is(equalTo(961)));
    }
}