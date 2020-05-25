package com.zone24x7.ibrac.recengine.pojo;

import java.util.LinkedList;
import java.util.List;

/**
 * Class for recommendation product curation status tracking.
 */
public class RecProductCurationStatus {
    private List<String> errorOccurredProducts = new LinkedList<>();
    private List<String> productInfoNonExistenceProducts = new LinkedList<>();
    private List<String> productInventoryNonExistenceProducts = new LinkedList<>();
    private List<String> productPriceNonExistenceProducts = new LinkedList<>();
    private List<String> curatedProducts = new LinkedList<>();

    /**
     * Method to get error occurred products
     *
     * @return error occurred products
     */
    public List<String> getErrorOccurredProducts() {
        return errorOccurredProducts;
    }

    /**
     * Method to get product info non existence products
     *
     * @return product info non existence products
     */
    public List<String> getProductInfoNonExistenceProducts() {
        return productInfoNonExistenceProducts;
    }

    /**
     * Method to get the product inventory non existence products
     *
     * @return product inventory non existence products
     */
    public List<String> getProductInventoryNonExistenceProducts() {
        return productInventoryNonExistenceProducts;
    }

    /**
     * Method to get product price non existence products
     *
     * @return product price non existence products
     */
    public List<String> getProductPriceNonExistenceProducts() {
        return productPriceNonExistenceProducts;
    }

    /**
     * Method to add error occurred products
     *
     * @param productId product id
     */
    public void addErrorOccurredProducts(String productId) {
        this.errorOccurredProducts.add(productId);
    }

    /**
     * Method to add product info non existence product id
     *
     * @param productId product info non existence product id
     */
    public void addProductInfoNonExistenceProduct(String productId) {
        this.productInfoNonExistenceProducts.add(productId);
    }

    /**
     * Method to add product inventory non existence product id
     *
     * @param productId product inventory non existence product id
     */
    public void addProductInventoryNonExistenceProduct(String productId) {
        this.productInventoryNonExistenceProducts.add(productId);
    }

    /**
     * Method to add product price non existence product id
     *
     * @param productId product price non existence product id
     */
    public void addProductPriceNonExistenceProduct(String productId) {
        this.productPriceNonExistenceProducts.add(productId);
    }

    /**
     * Method to get successfully curated products.
     *
     * @returnv curated products.
     */
    public List<String> getCuratedProducts() {
        return curatedProducts;
    }

    /**
     * Method to add curated products.
     *
     * @param productId id of the product curated.
     */
    public void addCuratedProduct(String productId) {
        this.curatedProducts.add(productId);
    }
}
