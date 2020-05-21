package com.zone24x7.ibrac.recengine.pojo;

/**
 * Model class to represent a single placeholder ID in the REST request
 *
 */
public class PlaceholderId {
    private String id;
    private Integer recommendationLimit;

    /**
     * Constructs an instance of the PlaceholderId
     *
     * @param builder the builder used to construct the PlaceholderId instance
     */
    public PlaceholderId(final PlaceholderIdBuilder builder){
        this.id = builder.id;
        this.recommendationLimit = builder.recommendationLimit;
    }

    /**
     * Gets the id of the placeholder
     *
     * @return the id of the placeholder
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the recommendation limit for the placeholder
     *
     * @return the recommendation limit for the placeholder
     */
    public Integer getRecommendationLimit() {
        return recommendationLimit;
    }

    /**
     * Placeholder Id builder to be populated from plids in recommendation requests
     *
     */
    public static class PlaceholderIdBuilder {
        private String id;
        private Integer recommendationLimit;

        /**
         * Sets the id of the placeholder for the placeholder id builder
         *
         * @param id the id of the placeholder
         * @return this placeholder id instance for the builder with id set
         */
        public PlaceholderIdBuilder id(final String id) {
            this.id = id;
            return this;
        }

        /**
         * Sets the recommendation limit of the placeholder for the placeholder id builder
         *
         * @param recommendationLimit the recommendation limit for the placeholder
         * @return this placeholder id instance for the builder with recommendation limit set
         */
        public PlaceholderIdBuilder recommendationLimit(final Integer recommendationLimit) {
            this.recommendationLimit = recommendationLimit;
            return this;
        }

        /**
         * Builds this placeholder id instance
         *
         * @return the instance of the built placeholder id instance
         */
        public PlaceholderId build() {
            return new PlaceholderId(this);
        }
    }

    /**
     * Equals override to make sure that two objects of the placeholder ID are equal only if the id is
     * equal
     *
     * @param obj the other object to check the equality against
     * @return true if id of both placeholder IDs are the same, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if (!(obj instanceof PlaceholderId)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        PlaceholderId other = (PlaceholderId)obj;

        return this.id.equals(other.id);
    }

    /**
     * Hash code override to make sure the hashcode of the object is based off it's id which is unique
     *
     * @return the hashcode of the placeholder id
     */
    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
