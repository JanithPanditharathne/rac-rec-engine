package com.zone24x7.ibrac.recengine.pojo.rules;

import com.zone24x7.ibrac.recengine.pojo.Product;

import java.util.List;
import java.util.Set;

/**
 * POJO class to represent the outcome of executing filtering rules on the recommendations
 */
public class FilteringRulesResult {
    private List<Product> filteredRecommendedProductsList;
    private Set<ExecutedRuleInfo> executedFilteringRuleInfoList;

    /**
     * Method to get the filtered recommended product list.
     *
     * @return the filtered recommended product list
     */
    public List<Product> getFilteredRecommendedProductsList() {
        return filteredRecommendedProductsList;
    }

    /**
     * Method to set the filtered recommended product list
     *
     * @param filteredRecommendedProductsList the filtered recommended product list
     */
    public void setFilteredRecommendedProductsList(List<Product> filteredRecommendedProductsList) {
        this.filteredRecommendedProductsList = filteredRecommendedProductsList;
    }

    /**
     * Method to get the executed filtering rules information.
     *
     * @return the executed filtering rules information
     */
    public Set<ExecutedRuleInfo> getExecutedFilteringRuleInfo() {
        return executedFilteringRuleInfoList;
    }

    /**
     * Method to set the executed filtering rules information
     *
     * @param executedFilteringRuleInfoList the executed filtering rules information
     */
    public void setExecutedFilteringRuleInfoList(Set<ExecutedRuleInfo> executedFilteringRuleInfoList) {
        this.executedFilteringRuleInfoList = executedFilteringRuleInfoList;
    }
}
