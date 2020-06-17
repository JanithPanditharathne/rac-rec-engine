package com.zone24x7.ibrac.recengine.pojo.csconfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

/**
 * Class to test Rec.
 */
public class RecTest {
    private Rec rec;
    private RegularConfig regularConfig = new RegularConfig();
    private TestConfig testConfig = new TestConfig();

    /**
     * Setup method
     */
    @BeforeEach
    void setUp() {
        rec = new Rec();
        rec.setId("1");
        rec.setName("Rec 1");
        rec.setType("Regular");
        rec.setRegularConfig(regularConfig);
        rec.setTestConfig(testConfig);
        rec.setMatchingCondition("department == \"shoes\"");
    }

    /**
     * Test to verify that attributes set and get are correctly done
     */
    @Test
    public void should_set_and_get_attributes_correctly() {
        assertThat(rec.getId(), is(equalTo("1")));
        assertThat(rec.getName(), is(equalTo("Rec 1")));
        assertThat(rec.getType(), is(equalTo("Regular")));
        assertThat(rec.getRegularConfig(), is(equalTo(regularConfig)));
        assertThat(rec.getTestConfig(), is(equalTo(testConfig)));
        assertThat(rec.getMatchingCondition(), is(equalTo("department == \"shoes\"")));
    }

    /**
     * Test to verify that equal comparison is functioning correctly
     */
    @Test
    public void should_check_equals_comparison_correctly() {
        assertThat(rec.equals(new Integer(3)), is(equalTo(false)));

        Rec rec2 = null;
        assertThat(rec.equals(rec2), is(equalTo(false)));

        rec2 = new Rec();
        assertThat(rec.equals(rec2), is(equalTo(false)));

        rec2.setId("1");
        assertThat(rec.equals(rec2), is(equalTo(false)));

        rec2.setName("Rec 1");
        assertThat(rec.equals(rec2), is(equalTo(false)));

        rec2.setType("Regular");
        assertThat(rec.equals(rec2), is(equalTo(false)));

        rec2.setRegularConfig(regularConfig);
        assertThat(rec.equals(rec2), is(equalTo(false)));

        rec2.setTestConfig(testConfig);
        assertThat(rec.equals(rec2), is(equalTo(false)));

        rec2.setMatchingCondition("department == \"shoes\"");
        assertThat(rec.equals(rec2), is(equalTo(true)));
    }

    /**
     * Test to verify that hash code generation is correctly done
     */
    @Test
    public void should_generate_the_hash_code_correctly() {
        assertThat(rec.hashCode(), is(equalTo(2134611166)));
    }
}