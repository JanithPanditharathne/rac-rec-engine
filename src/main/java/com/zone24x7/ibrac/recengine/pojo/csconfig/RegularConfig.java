package com.zone24x7.ibrac.recengine.pojo.csconfig;

import javax.validation.constraints.NotEmpty;

/**
 * Class to represent the regular config.
 */
public class RegularConfig {
    @NotEmpty
    private String bundleId;

    /**
     * Method to get the bundle id.
     *
     * @return the bundle id
     */
    public String getBundleId() {
        return bundleId;
    }

    /**
     * Method to set the bundle id.
     *
     * @param bundleId the bundle id
     */
    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    /**
     * Overridden equals method
     *
     * @param o object to compare
     * @return true if equal and false if not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RegularConfig that = (RegularConfig) o;

        return bundleId != null ? bundleId.equals(that.bundleId) : (that.bundleId == null);
    }

    /**
     * Overridden hash code method
     *
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return bundleId != null ? bundleId.hashCode() : 0;
    }
}
