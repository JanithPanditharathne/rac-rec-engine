package com.zone24x7.ibrac.recengine.pojo.csconfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

/**
 * Class to test RecSlotConfig.
 */
public class RecSlotConfigTest  {
    private RecSlotConfig recSlotConfig;
    private List<RecSlot> recSlotList = new LinkedList<>();
    private RecSlot recSlot1 = new RecSlot();
    private RecSlot recSlot2 = new RecSlot();

    /**
     * Setup method
     */
    @BeforeEach
    void setUp() {
        recSlotConfig = new RecSlotConfig();

        recSlotList.add(recSlot1);
        recSlotList.add(recSlot2);

        recSlotConfig.setRecSlots(recSlotList);
    }

    /**
     * Test to verify that attributes set and get are correctly done
     */
    @Test
    public void should_set_and_get_attributes_correctly() {
        assertThat(recSlotConfig.getRecSlots(), is(equalTo(recSlotList)));
    }

    /**
     * Test to verify that equal comparison is functioning correctly
     */
    @Test
    public void should_check_equals_comparison_correctly() {
        assertThat(recSlotConfig.equals(new Integer(3)), is(equalTo(false)));

        RecSlotConfig recSlotConfig2 = null;
        assertThat(recSlotConfig.equals(recSlotConfig2), is(equalTo(false)));

        recSlotConfig2 = new RecSlotConfig();
        assertThat(recSlotConfig.equals(recSlotConfig2), is(equalTo(false)));
        assertThat(recSlotConfig2.equals(recSlotConfig), is(equalTo(false)));

        recSlotConfig2.setRecSlots(Collections.emptyList());
        assertThat(recSlotConfig.equals(recSlotConfig2), is(equalTo(false)));
        assertThat(recSlotConfig2.equals(recSlotConfig), is(equalTo(false)));

        recSlotConfig2.setRecSlots(recSlotList);
        assertThat(recSlotConfig.equals(recSlotConfig2), is(equalTo(true)));
        assertThat(recSlotConfig2.equals(recSlotConfig), is(equalTo(true)));
    }

    /**
     * Test to verify that hash code generation is correctly done
     */
    @Test
    public void should_generate_the_hash_code_correctly() {
        assertThat(recSlotConfig.hashCode(), is(equalTo(961)));
    }
}