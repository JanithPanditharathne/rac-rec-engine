package com.zone24x7.ibrac.recengine.pojo.recbundle;

import com.zone24x7.ibrac.recengine.pojo.csconfig.Bundle;
import com.zone24x7.ibrac.recengine.pojo.csconfig.RecSlot;

import java.util.Map;

/**
 * Class to represent the active bundle provider configuration.
 */
public class ActiveBundleProviderConfig {
    private Map<String, Bundle> bundleMap;
    private Map<String, RecSlot> recSlotMap;

    /**
     * Method to get the bundle map.
     *
     * @return the bundle map
     */
    public Map<String, Bundle> getBundleMap() {
        return bundleMap;
    }

    /**
     * Method to set the bundle map.
     *
     * @param bundleMap the bundle map
     */
    public void setBundleMap(Map<String, Bundle> bundleMap) {
        this.bundleMap = bundleMap;
    }

    /**
     * Method to get the rec slot map.
     *
     * @return the rec slot map
     */
    public Map<String, RecSlot> getRecSlotMap() {
        return recSlotMap;
    }

    /**
     * Method to set the rec slot map.
     *
     * @param recSlotMap the rec slot map
     */
    public void setRecSlotMap(Map<String, RecSlot> recSlotMap) {
        this.recSlotMap = recSlotMap;
    }
}
