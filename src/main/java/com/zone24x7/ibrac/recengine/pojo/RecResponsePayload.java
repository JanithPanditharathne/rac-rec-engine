package com.zone24x7.ibrac.recengine.pojo;

import java.util.List;

/**
 * Class to represent rec response payload.
 */
public class RecResponsePayload {
    private String cid;
    private String pgId;
    private List<RecResult> recommendations;

    /**
     * Gets channel id.
     *
     * @return channel id.
     */
    public String getCid() {
        return cid;
    }

    /**
     * Sets channel id.
     *
     * @param cid channel id.
     */
    public void setCid(String cid) {
        this.cid = cid;
    }

    /**
     * Gets page id.
     *
     * @return page id.
     */
    public String getPgId() {
        return pgId;
    }

    /**
     * Sets page id.
     *
     * @param pgId page id.
     */
    public void setPgId(String pgId) {
        this.pgId = pgId;
    }

    /**
     * Sets list of recommendations per placement.
     *
     * @return list of recommendations per placement.
     */
    public List<RecResult> getRecommendations() {
        return recommendations;
    }

    /**
     * Sets list of recommendations per placement.
     *
     * @param recommendations list of recommendations per placement.
     */
    public void setRecommendations(List<RecResult> recommendations) {
        this.recommendations = recommendations;
    }
}
