package com.zone24x7.ibrac.recengine.pojo;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class to represent the product.
 */
public class Product {
    private Map<String, String> attributesMap = new LinkedHashMap<>();

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
}
