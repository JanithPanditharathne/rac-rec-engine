package com.zone24x7.ibrac.recengine.pojo;

import com.zone24x7.ibrac.recengine.enumeration.RecommendationType;
import com.zone24x7.ibrac.recengine.pojo.rules.ExecutedRuleInfo;

import java.util.Map;
import java.util.Set;

/**
 * Class to represent meta information of flat recommendations.
 */
public class RecMetaInfo {
    private RecommendationType type;
    private String bundleId;
    private int limitToApply;
    private Map<String, String> algoToProductsMap;
    private Map<String, String> algoToUsedCcp;
    private Set<ExecutedRuleInfo> executedFilteringRuleInfoList;

    /**
     * Method to return the type of recommendations
     *
     * @return type of recommendation
     */
    public RecommendationType getType() {
        return type;
    }

    /**
     * Sets the type of recommendation
     *
     * @param type type to set
     */
    public void setType(RecommendationType type) {
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
     * Method to set ExecutedRuleInfo
     *
     * @return ExecutedRuleInfo set
     */
    public Set<ExecutedRuleInfo> getExecutedFilteringRuleInfoList() {
        return executedFilteringRuleInfoList;
    }

    /**
     * Method to return the ExecutedRuleInfo set
     *
     * @param executedFilteringRuleInfoList ExecutedRuleInfo set to set
     */
    public void setExecutedFilteringRuleInfoList(Set<ExecutedRuleInfo> executedFilteringRuleInfoList) {
        this.executedFilteringRuleInfoList = executedFilteringRuleInfoList;
    }

    /**
     * Returns the limit to apply
     *
     * @return the limit to apply
     */
    public int getLimitToApply() {
        return limitToApply;
    }

    /**
     * Sets the limit to apply
     *
     * @param limitToApply limit to apply
     */
    public void setLimitToApply(int limitToApply) {
        this.limitToApply = limitToApply;
    }
}
