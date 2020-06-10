package com.zone24x7.ibrac.recengine.pojo;

import java.util.BitSet;

/**
 * Class to represent the status of the recommendation generation cycle.
 */
public class RecCycleStatus {
    private BitSet cycleMask;
    private String requestId;

    // Constants for the Bit numbers
    private static final int BIT_0 = 0;
    private static final int BIT_1 = 1;
    private static final int BIT_2 = 2;
    private static final int BIT_3 = 3;
    private static final int BIT_4 = 4;
    private static final int BIT_5 = 5;
    private static final int BIT_6 = 6;
    private static final int BIT_7 = 7;

    /**
     * Constructor to instantiate RecCycleStatus.
     *
     * @param requestId the request id
     */
    public RecCycleStatus(String requestId) {
        this.requestId = requestId;
        this.cycleMask = new BitSet();
    }

    /**
     * Method to indicate that channel placement rules found for input context.
     */
    public void indicateChannelPlacementRuleFoundForInputContext() {
        this.cycleMask.set(BIT_0);
    }

    /**
     * Method to indicate that active bundle found.
     */
    public void indicateActiveBundleFound() {
        this.cycleMask.set(BIT_1);
    }

    /**
     * Method to indicate that HBase returned recommendations for at least one HBase call
     */
    public void indicateHBaseReturnedRecs() {
        this.cycleMask.set(BIT_2);
    }

    /**
     * Method to indicate that curation removed all products.
     */
    public void indicateCurationRemovedAllProducts() {
        this.cycleMask.set(BIT_3);
    }

    /**
     * Method to indicate that filtering rules got applied.
     */
    public void indicateFilteringRulesGotApplied() {
        this.cycleMask.set(BIT_4);
    }

    /**
     * Method to indicate that filtering rules removed all products
     */
    public void indicateFilteringRulesRemovedAllProducts() {
        this.cycleMask.set(BIT_5);
    }

    /**
     * Method to indicate that no recommendations are generated for algorithms.
     */
    public void indicateNoRecsGeneratedForAlgos() {
        this.cycleMask.set(BIT_6);
    }

 /**
     * Method to indicate that there was at lease one exception happened with HBase.
     */
    public void indicateExceptionInCallingHBase() {
        this.cycleMask.set(BIT_7);
    }

    /**
     * Get the recommendation generation cycle status
     *
     * @return a long value representing the recommendation generation cycle information
     */
    public long getRecGenerationCycleMask() {
        long value = 0L;

        for (int i = 0; i < this.cycleMask.length(); ++i) {
            value += this.cycleMask.get(i) ? (1L << i) : 0L;
        }

        return value;
    }

    /**
     * Setter for Mask
     *
     * @param mask Bit Mask
     */
    public void setRecGenerationCycleMask(BitSet mask) {
        this.cycleMask = mask;
    }

    /**
     * Method to get the request id.
     *
     * @return the request id.
     */
    public String getRequestId() {
        return requestId;
    }
}
