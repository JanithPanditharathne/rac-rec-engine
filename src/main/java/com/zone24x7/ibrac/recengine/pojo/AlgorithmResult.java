package com.zone24x7.ibrac.recengine.pojo;

import java.util.LinkedList;
import java.util.List;

public class AlgorithmResult {
    private List<Product> recProducts = new LinkedList<>();

    public List<Product> getRecProducts() {
        return recProducts;
    }

    public void setRecProducts(List<Product> recProducts) {
        this.recProducts = recProducts;
    }
}

