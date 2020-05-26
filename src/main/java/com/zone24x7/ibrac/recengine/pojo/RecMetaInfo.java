package com.zone24x7.ibrac.recengine.pojo;

import com.zone24x7.ibrac.recengine.pojo.rules.EdeExecutedRuleInfo;

import java.util.Map;
import java.util.Set;

/**
 * Class to represent meta information of flat recommendations.
 */
public class RecMetaInfo {
    private String type;
    private String bundleId;
    private Map<String, String> algoToProductsMap;
    private Map<String, String> algoToUsedCcp;
    private Set<EdeExecutedRuleInfo> executedFilteringRuleInfoList;

    /**
     * Method to return the type of recommendations
     *
     * @return type of recommendation
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of recommedation
     *
     * @param type type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the bundle id
     *
     * @return bundle Id
     */
    public String getBundleId() {
        return bundleId;
    }

    /**
     * Method to set the bundle id
     *
     * @param bundleId bundle id to set
     */
    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    /**
     * Method to get the algoToProduct map
     *
     * @return algoToProduct map
     */
    public Map<String, String> getAlgoToProductsMap() {
        return algoToProductsMap;
    }

    /**
     * Method to set algoToProduct map
     *
     * @param algoToProductsMap algoToProduct map
     */
    public void setAlgoToProductsMap(Map<String, String> algoToProductsMap) {
        this.algoToProductsMap = algoToProductsMap;
    }

    /**
     * Method to get the getAlgoToUsedCcp map
     *
     * @return getAlgoToUsedCcp
     */
    public Map<String, String> getAlgoToUsedCcp() {
        return algoToUsedCcp;
    }

    /**
     * Method to set the getAlgoToUsedCcp map
     *
     * @param algoToUsedCcp getAlgoToUsedCcp map to set
     */
    public void setAlgoToUsedCcp(Map<String, String> algoToUsedCcp) {
        this.algoToUsedCcp = algoToUsedCcp;
    }

    /**
     * Method to set EdeExecutedRuleInfo
     *
     * @return EdeExecutedRuleInfo set
     */
    public Set<EdeExecutedRuleInfo> getExecutedFilteringRuleInfoList() {
        return executedFilteringRuleInfoList;
    }

    /**
     * Method to return the EdeExecutedRuleInfo set
     *
     * @param executedFilteringRuleInfoList EdeExecutedRuleInfo set to set
     */
    public void setExecutedFilteringRuleInfoList(Set<EdeExecutedRuleInfo> executedFilteringRuleInfoList) {
        this.executedFilteringRuleInfoList = executedFilteringRuleInfoList;
    }
}