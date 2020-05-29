package com.zone24x7.ibrac.recengine.enumeration;

public enum RecommendationType {
    /**
     * Flat recommendations experience.
     * This recommendation type comes with set of products and a display text
     */
    FLAT_RECOMMENDATION("FlatRec");

    private String recTypeName;

    /**
     * Method to set the recommendation type name
     *
     * @param recTypeName recommendation type name
     */
    RecommendationType(String recTypeName) {
        this.recTypeName = recTypeName;
    }

    /**
     * Method to return the rec type name
     *
     * @return rec type name
     */
    public String getRecTypeName() {
        return recTypeName;
    }
}
