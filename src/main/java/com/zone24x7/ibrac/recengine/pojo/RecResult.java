package com.zone24x7.ibrac.recengine.pojo;

/**
 * Class to represent the recommendation result.
 *
 * @param <P> the payload object type
 * @param <M> the meta info object type
 */
public class RecResult<P, M> {
    // Recommendation payload
    private P recPayload;
    // Object containing meta information related to recommendation generation
    private M recMetaInfo;

    /**
     * Method to get the recommendation payload.
     *
     * @return the recommendation payload
     */
    public P getRecPayload() {
        return recPayload;
    }

    /**
     * Method to set the recommendation payload.
     *
     * @param recPayload the recommendation payload
     */
    public void setRecPayload(P recPayload) {
        this.recPayload = recPayload;
    }

    /**
     * Method to get the recommendation meta information.
     *
     * @return the recommendation meta information
     */
    public M getRecMetaInfo() {
        return recMetaInfo;
    }

    /**
     * Method to set the recommendation meta information.
     *
     * @param recMetaInfo the recommendation meta information to set
     */
    public void setRecMetaInfo(M recMetaInfo) {
        this.recMetaInfo = recMetaInfo;
    }
}
