package com.zone24x7.ibrac.recengine.pojo;

import com.zone24x7.ibrac.recengine.pojo.rules.FilteringRulesResult;

/**
 * Class to represent recommendation status parameters.
 */
public class RecStatusParams {
    private int limit;
    private RecCycleStatus recCycleStatus;
    private MultipleAlgorithmResult multipleAlgorithmResult;
    private FilteringRulesResult filteringRulesResult;
    private RecResult<FlatRecPayload> recResult;

    /**
     * Method to get the limit.
     *
     * @return the limit
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Method to set the limit.
     *
     * @param limit the limit to set
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Method to get the recommendation generation tracking object.
     *
     * @return the recommendation generation tracking object
     */
    public RecCycleStatus getRecCycleStatus() {
        return recCycleStatus;
    }

    /**
     * Merhod to set the recommendation generation tracking object.
     *
     * @param recCycleStatus the recommendation generation tracking object
     */
    public void setRecCycleStatus(RecCycleStatus recCycleStatus) {
        this.recCycleStatus = recCycleStatus;
    }

    /**
     * Method to get the recommendation result.
     *
     * @return the recommendation result
     */
    public RecResult<FlatRecPayload> getRecResult() {
        return recResult;
    }

    /**
     * Method to set the recommendation result.
     *
     * @param recResult the recommendation result
     */
    public void setRecResult(RecResult<FlatRecPayload> recResult) {
        this.recResult = recResult;
    }

    /**
     * Method to return MultipleAlgorithmResult
     *
     * @return MultipleAlgorithmResult object
     */
    public MultipleAlgorithmResult getMultipleAlgorithmResult() {
        return multipleAlgorithmResult;
    }

    /**
     * Method to set multipleAlgorithmResult
     *
     * @param multipleAlgorithmResult multipleAlgorithmResult object to set
     */
    public void setMultipleAlgorithmResult(MultipleAlgorithmResult multipleAlgorithmResult) {
        this.multipleAlgorithmResult = multipleAlgorithmResult;
    }

    /**
     * Method to set the filtering rule result
     *
     * @param filteringRulesResult filtering rule result
     */
    public void setFilteringRulesResult(FilteringRulesResult filteringRulesResult) {
        this.filteringRulesResult = filteringRulesResult;
    }

    /**
     * Method to get a filtering rule result
     *
     * @return filtering rules
     */
    public FilteringRulesResult getFilteringRuleResult() {
        return filteringRulesResult;
    }
}
