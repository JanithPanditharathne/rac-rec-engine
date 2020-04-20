package com.zone24x7.ibrac.recengine.pojo;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class to represent the product.
 */
public class Product {
    private Map<String, String> attributes = new LinkedHashMap<>();

    /**
     * Method to get the product attributes.
     *
     * @return the product attributes
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * Method to set the product attributes
     *
     * @param attributes the product attributes to set
     */
    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
