package com.zone24x7.ibrac.recengine.pojo;

/**
 * Class to represent recommendation status parameters.
 */
public class RecStatusParams {
    private int limit;
    private RecCycleStatus recCycleStatus;
    private RecResult<FlatRecPayload, FlatRecMetaInfo> recResult;

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
    public RecResult<FlatRecPayload, FlatRecMetaInfo> getRecResult() {
        return recResult;
    }

    /**
     * Method to set the recommendation result.
     *
     * @param recResult the recommendation result
     */
    public void setRecResult(RecResult<FlatRecPayload, FlatRecMetaInfo> recResult) {
        this.recResult = recResult;
    }
}
