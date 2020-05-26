package com.zone24x7.ibrac.recengine.pojo;

/**
 * Class to represent the recommendation result.
 *
 * @param <P> the payload object type
 */
public class RecResult<P> {
    // Recommendation payload
    private P recPayload;
    // Object containing meta information related to recommendation generation
    private RecMetaInfo recMetaInfo;

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
     * Method to return the meta info
     *
     * @return rec meta info
     */
    public RecMetaInfo getRecMetaInfo() {
        return recMetaInfo;
    }

    /**
     * Method to set meta info
     *
     * @param recMetaInfo meta info to set
     */
    public void setRecMetaInfo(RecMetaInfo recMetaInfo) {
        this.recMetaInfo = recMetaInfo;
    }
}
