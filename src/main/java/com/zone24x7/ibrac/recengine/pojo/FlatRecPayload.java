package com.zone24x7.ibrac.recengine.pojo;

import java.util.List;

/**
 * Class to represent payload for flat recommendations.
 */
public class FlatRecPayload {
    private String displayText;
    private List<Product> products;

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

    /**
     * Method to return the list of products
     *
     * @return list of products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * Return the list of products
     *
     * @param products list of products to return
     */
    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
