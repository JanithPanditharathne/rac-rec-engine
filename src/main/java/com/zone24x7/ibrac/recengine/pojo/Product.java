package com.zone24x7.ibrac.recengine.pojo;

import java.util.LinkedHashMap;
import java.util.Map;

public class Product {
    private Map<String, String> attributes = new LinkedHashMap<>();

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
