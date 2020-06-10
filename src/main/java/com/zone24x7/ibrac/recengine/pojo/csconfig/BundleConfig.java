package com.zone24x7.ibrac.recengine.pojo.csconfig;

import javax.validation.Valid;
import java.util.List;

/**
 * Class to represent bundle configuration.
 */
public class BundleConfig {
    @Valid
    private List<Bundle> bundles;

    /**
     * Method to get the bundles.
     *
     * @return the bundles
     */
    public List<Bundle> getBundles() {
        return bundles;
    }

    /**
     * Method to set the bundles.
     *
     * @param bundles the bundles to set
     */
    public void setBundles(List<Bundle> bundles) {
        this.bundles = bundles;
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

        BundleConfig that = (BundleConfig) o;

        return bundles != null ? bundles.equals(that.bundles) : (that.bundles == null);
    }

    /**
     * Overridden hash code method
     *
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return bundles != null ? bundles.hashCode() : 0;
    }
}
