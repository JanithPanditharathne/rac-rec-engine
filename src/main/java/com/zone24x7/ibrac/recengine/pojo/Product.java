package com.zone24x7.ibrac.recengine.pojo;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class to represent the product.
 */
public class Product {
    private String productId;
    private Map<String, String> attributesMap = new LinkedHashMap<>();

    private static final int HASH_SEED = 31;

    /**
     * Method to get id.
     *
     * @return id of the product.
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Method to set id of the product.
     *
     * @param productId of the product.
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * Method to get the product attributes.
     *
     * @return the product attributes
     */
    public Map<String, String> getAttributesMap() {
        return attributesMap;
    }

    /**
     * Method to set the product attributes
     *
     * @param attributesMap the product attributes to set
     */
    public void setAttributesMap(Map<String, String> attributesMap) {
        this.attributesMap = attributesMap;
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

        Product product = (Product) o;

        if (productId != null ? !productId.equals(product.productId) : (product.productId != null)) {
            return false;
        }

        return attributesMap != null ? attributesMap.equals(product.attributesMap) : (product.attributesMap == null);
    }

    /**
     * Overridden hash code method
     *
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        int result = productId != null ? productId.hashCode() : 0;
        result = HASH_SEED * result + (attributesMap != null ? attributesMap.hashCode() : 0);
        return result;
    }
}
