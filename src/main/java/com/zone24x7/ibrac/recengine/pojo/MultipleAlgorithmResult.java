package com.zone24x7.ibrac.recengine.pojo;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Pojo class to represent the multiple algorithm results.
 */
public class MultipleAlgorithmResult {
    private List<Product> recProducts = new LinkedList<>();
    private Map<String, String> algoToProductsMap = new LinkedHashMap<>();
    private Map<String, String> algoToUsedCcp = new LinkedHashMap<>();
    private String displayText;

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
        this.recProducts.addAll(recProducts);
    }

    /**
     * Returns the algo product map
     *
     * @return algo product map
     */
    public Map<String, String> getAlgoToProductsMap() {
        return algoToProductsMap;
    }

    /**
     * Sets the algo product map
     *
     * @param algoToProductsMap algoToProductsMap to set
     */
    public void addToAlgoToProductsMap(Map<String, String> algoToProductsMap) {
        this.algoToProductsMap.putAll(algoToProductsMap);
    }

    /**
     * Method to get used ccp
     *
     * @return used ccp
     */
    public Map<String, String> getAlgoToUsedCcp() {
        return algoToUsedCcp;
    }

    /**
     * Method to set used ccps
     *
     * @param algoToUsedCcp used ccp to add
     */
    public void addToAlgoToUsedCcp(Map<String, String> algoToUsedCcp) {
        this.algoToUsedCcp.putAll(algoToUsedCcp);
    }

    /**
     * Method to get the display text
     *
     * @return the display text
     */
    public String getDisplayText() {
        return displayText;
    }

    /**
     * Sets the display text
     *
     * @param displayText display text to set
     */
    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }
}

