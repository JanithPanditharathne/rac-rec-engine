package com.zone24x7.ibrac.recengine.pojo;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class to represent the algorithm result.
 */
public class AlgorithmResult {
    private String algorithmId;
    private List<Product> recProducts = new LinkedList<>();
    private Map<String, String> usedCcp = new LinkedHashMap<>();

    /**
     * Method to get the recommendation products.
     *
     * @return the recommendation products
     */
    public List<Product> getRecProducts() {
        return recProducts;
    }

    /**
     * Method to set the recommendation products.
     *
     * @param recProducts the recommendation products to set
     */
    public void setRecProducts(List<Product> recProducts) {
        if (recProducts != null) {
            this.recProducts.addAll(recProducts);
        }
    }

    /**
     * Get used ccp map
     *
     * @return map of used ccps
     */
    public Map<String, String> getUsedCcp() {
        return usedCcp;
    }

    /**
     * Set used ccp
     *
     * @param usedCcp map of used ccps
     */
    public void setUsedCcp(Map<String, String> usedCcp) {
        this.usedCcp = usedCcp;
    }

    /**
     * Get algorithm Id
     *
     * @return algorithm id
     */
    public String getAlgorithmId() {
        return algorithmId;
    }

    /**
     * Sets algorithm id
     *
     * @param algorithmId algo id to set
     */
    public void setAlgorithmId(String algorithmId) {
        this.algorithmId = algorithmId;
    }
}

