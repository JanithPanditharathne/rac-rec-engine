package com.zone24x7.ibrac.recengine.pojo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.BitSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

/**
 * Class to test RecCycleStatus.
 */
class RecCycleStatusTest {
    private RecCycleStatus recCycleStatus;

    /**
     * Setup method
     */
    @BeforeEach
    void setUp() {
        recCycleStatus = new RecCycleStatus("1234");
    }

    /**
     * Test to verify that request id is returned correctly.
     */
    @Test
    public void should_return_the_request_id_correctly() {
        assertThat(recCycleStatus.getRequestId(), is(equalTo("1234")));
    }

    /**
     * Test to verify that request cycle mask is generated correctly.
     */
    @Test
    public void should_return_the_cycle_mask_correctly() {
        recCycleStatus.indicateCurationRemovedAllProducts();
        recCycleStatus.indicateExceptionInCallingHBase();
        recCycleStatus.indicateHBaseReturnedRecs();
        recCycleStatus.indicateActiveBundleFound();
        recCycleStatus.indicateChannelPlacementRuleFoundForInputContext();
        recCycleStatus.indicateFilteringRulesGotApplied();
        recCycleStatus.indicateFilteringRulesRemovedAllProducts();
        recCycleStatus.indicateNoRecsGeneratedForAlgos();

        assertThat(recCycleStatus.getRecGenerationCycleMask(), is(equalTo(255L)));
    }

    /**
     * Test to verify that request cycle mask is generated correctly when bit is set externally.
     */
    @Test
    public void should_return_the_cycle_mask_correctly_when_bit_is_set_externally() {
        BitSet bitSet = new BitSet();
        bitSet.set(1);
        bitSet.set(3);
        bitSet.set(5);
        recCycleStatus.setRecGenerationCycleMask(bitSet);
        assertThat(recCycleStatus.getRecGenerationCycleMask(), is(equalTo(42L)));
    }

}
