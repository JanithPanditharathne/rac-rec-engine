package com.zone24x7.ibrac.recengine.pojo;

/**
 * Represents the recommendation result response.
 */
public class RecResponse {
    private RecResponsePayload payload;

    /**
     * Constructor of RecResponse.
     *
     * @param payload rec response payload.
     */
    public RecResponse(RecResponsePayload payload) {
        this.payload = payload;
    }

    /**
     * Gets rec result response payload.
     *
     * @return result response payload.
     */
    public RecResponsePayload getPayload() {
        return payload;
    }

    /**
     * Sets rec result response payload.
     *
     * @param payload rec result response payload.
     */
    public void setPayload(RecResponsePayload payload) {
        this.payload = payload;
    }
}
