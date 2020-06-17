package com.zone24x7.ibrac.recengine.pojo.csconfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

/**
 * Class to test RecSlot.
 */
public class RecSlotTest {
    private RecSlot recSlot;
    private List<String> recIdList = new LinkedList<>();
    private List<String> ruleIdList = new LinkedList<>();

    /**
     * Setup method
     */
    @BeforeEach
    void setUp() {
        recSlot = new RecSlot();
        recSlot.setChannel("Web");
        recSlot.setPage("Home");
        recSlot.setPlaceholder("Horizontal");
        recSlot.setRecIds(recIdList);
        recSlot.setRuleIds(ruleIdList);
    }

    /**
     * Test to verify that attributes set and get are correctly done
     */
    @Test
    public void should_set_and_get_attributes_correctly() {
        assertThat(recSlot.getChannel(), is(equalTo("Web")));
        assertThat(recSlot.getPage(), is(equalTo("Home")));
        assertThat(recSlot.getPlaceholder(), is(equalTo("Horizontal")));
        assertThat(recSlot.getRecIds(), is(equalTo(recIdList)));
        assertThat(recSlot.getRuleIds(), is(equalTo(ruleIdList)));
    }

    /**
     * Test to verify that equal comparison is functioning correctly
     */
    @Test
    public void should_check_equals_comparison_correctly() {
        assertThat(recSlot.equals(new Integer(3)), is(equalTo(false)));

        RecSlot recSlot2 = null;
        assertThat(recSlot.equals(recSlot2), is(equalTo(false)));

        recSlot2 = new RecSlot();
        assertThat(recSlot.equals(recSlot2), is(equalTo(false)));

        recSlot2.setChannel("Web");
        assertThat(recSlot.equals(recSlot2), is(equalTo(false)));

        recSlot2.setPage("Home");
        assertThat(recSlot.equals(recSlot2), is(equalTo(false)));

        recSlot2.setPlaceholder("Horizontal");
        assertThat(recSlot.equals(recSlot2), is(equalTo(false)));

        recSlot2.setRecIds(recIdList);
        assertThat(recSlot.equals(recSlot2), is(equalTo(false)));

        recSlot2.setRuleIds(ruleIdList);
        assertThat(recSlot.equals(recSlot2), is(equalTo(true)));
    }

    /**
     * Test to verify that hash code generation is correctly done
     */
    @Test
    public void should_generate_the_hash_code_correctly() {
        assertThat(recSlot.hashCode(), is(equalTo(-710704327)));
    }

    @Test
    public void should_get_placement_info_string_correctly() {
        String placementInfoAsString = recSlot.getPlacementInfoAsString();
        assertThat(placementInfoAsString, is(equalTo("Web_Home_Horizontal")));

    }
}