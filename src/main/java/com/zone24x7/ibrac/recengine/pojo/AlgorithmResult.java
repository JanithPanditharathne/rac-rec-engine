package com.zone24x7.ibrac.recengine.pojo;

import java.util.LinkedList;
import java.util.List;

/**
 * Class to represent the algorithm result.
 */
public class AlgorithmResult {
    private List<Product> recProducts = new LinkedList<>();

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
        this.recProducts = recProducts;
    }
}

