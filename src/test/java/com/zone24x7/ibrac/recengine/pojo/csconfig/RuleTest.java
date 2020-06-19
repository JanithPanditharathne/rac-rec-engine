package com.zone24x7.ibrac.recengine.pojo.csconfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

/**
 * Class to test Rule.
 */
public class RuleTest {
    private Rule rule;

    /**
     * Setup method
     */
    @BeforeEach
    void setUp() {
       rule = new Rule();
       rule.setId("1");
       rule.setName("Rule 1");
       rule.setType("Boost");
       rule.setGlobal(true);
       rule.setActionCondition("department == \"shoes\"");
       rule.setMatchingCondition("category == \"clothing\"");
    }

    /**
     * Test to verify that attributes set and get are correctly done
     */
    @Test
    public void should_set_and_get_attributes_correctly() {
        assertThat(rule.getId(), is(equalTo("1")));
        assertThat(rule.getName(), is(equalTo("Rule 1")));
        assertThat(rule.getType(), is(equalTo("Boost")));
        assertThat(rule.isGlobal(), is(equalTo(true)));
        assertThat(rule.getActionCondition(), is(equalTo("department == \"shoes\"")));
        assertThat(rule.getMatchingCondition(), is(equalTo("category == \"clothing\"")));
    }

    /**
     * Test to verify that equal comparison is functioning correctly
     */
    @Test
    public void should_check_equals_comparison_correctly() {
        assertThat(rule.equals(new Integer(3)), is(equalTo(false)));

        Rule rule2 = null;
        assertThat(rule.equals(rule2), is(equalTo(false)));

        rule2 = new Rule();
        assertThat(rule.equals(rule2), is(equalTo(false)));
        assertThat(rule2.equals(rule), is(equalTo(false)));

        rule2.setId("Invalid id");
        assertThat(rule.equals(rule2), is(equalTo(false)));
        assertThat(rule2.equals(rule), is(equalTo(false)));

        rule2.setId("1");
        assertThat(rule.equals(rule2), is(equalTo(false)));
        assertThat(rule2.equals(rule), is(equalTo(false)));

        rule2.setName("Invalid Rule name");
        assertThat(rule.equals(rule2), is(equalTo(false)));
        assertThat(rule2.equals(rule), is(equalTo(false)));

        rule2.setName("Rule 1");
        assertThat(rule.equals(rule2), is(equalTo(false)));
        assertThat(rule2.equals(rule), is(equalTo(false)));

        rule2.setType("Invalid Type");
        assertThat(rule.equals(rule2), is(equalTo(false)));
        assertThat(rule2.equals(rule), is(equalTo(false)));

        rule2.setType("Boost");
        assertThat(rule.equals(rule2), is(equalTo(false)));
        assertThat(rule2.equals(rule), is(equalTo(false)));

        rule2.setGlobal(false);
        assertThat(rule.equals(rule2), is(equalTo(false)));
        assertThat(rule2.equals(rule), is(equalTo(false)));

        rule2.setGlobal(true);
        assertThat(rule.equals(rule2), is(equalTo(false)));
        assertThat(rule2.equals(rule), is(equalTo(false)));

        rule2.setActionCondition("department == \"invalid\"");
        assertThat(rule.equals(rule2), is(equalTo(false)));
        assertThat(rule2.equals(rule), is(equalTo(false)));

        rule2.setActionCondition("department == \"shoes\"");
        assertThat(rule.equals(rule2), is(equalTo(false)));
        assertThat(rule2.equals(rule), is(equalTo(false)));

        rule2.setMatchingCondition("category == \"invalid\"");
        assertThat(rule.equals(rule2), is(equalTo(false)));
        assertThat(rule2.equals(rule), is(equalTo(false)));

        rule2.setMatchingCondition("category == \"clothing\"");
        assertThat(rule.equals(rule2), is(equalTo(true)));
        assertThat(rule2.equals(rule), is(equalTo(true)));
    }

    /**
     * Test to verify that hash code generation is correctly done
     */
    @Test
    public void should_generate_the_hash_code_correctly() {
        assertThat(rule.hashCode(), is(equalTo(1556124970)));
    }
}