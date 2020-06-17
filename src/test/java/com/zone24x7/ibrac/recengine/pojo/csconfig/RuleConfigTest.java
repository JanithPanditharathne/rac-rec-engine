package com.zone24x7.ibrac.recengine.pojo.csconfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

/**
 * Class to test RuleConfig.
 */
public class RuleConfigTest {
    private RuleConfig ruleConfig;
    private List<Rule> ruleList = new LinkedList<>();
    private Rule rule1 = new Rule();
    private Rule rule2 = new Rule();

    /**
     * Setup method
     */
    @BeforeEach
    void setUp() {
       ruleConfig = new RuleConfig();

       ruleList.add(rule1);
       ruleList.add(rule2);
       ruleConfig.setRules(ruleList);
    }

    /**
     * Test to verify that attributes set and get are correctly done
     */
    @Test
    public void should_set_and_get_attributes_correctly() {
        assertThat(ruleConfig.getRules(), is(equalTo(ruleList)));
    }

    /**
     * Test to verify that equal comparison is functioning correctly
     */
    @Test
    public void should_check_equals_comparison_correctly() {
        assertThat(ruleConfig.equals(new Integer(3)), is(equalTo(false)));

        RuleConfig ruleConfig2 = null;
        assertThat(ruleConfig.equals(ruleConfig2), is(equalTo(false)));

        ruleConfig2 = new RuleConfig();
        assertThat(ruleConfig.equals(ruleConfig2), is(equalTo(false)));

        ruleConfig2.setRules(ruleList);
        assertThat(ruleConfig.equals(ruleConfig2), is(equalTo(true)));
    }

    /**
     * Test to verify that hash code generation is correctly done
     */
    @Test
    public void should_generate_the_hash_code_correctly() {
        assertThat(ruleConfig.hashCode(), is(equalTo(-109159935)));
    }
}