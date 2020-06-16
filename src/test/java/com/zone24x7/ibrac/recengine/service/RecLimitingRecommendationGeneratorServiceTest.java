package com.zone24x7.ibrac.recengine.service;


import com.zone24x7.ibrac.recengine.pojo.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * Test class for RecLimitingRecommendationGeneratorService
 */
public class RecLimitingRecommendationGeneratorServiceTest {

    RecLimitingRecommendationGeneratorService recLimitingRecommendationGeneratorService;
    private List<Product> duplicateProductList;
    private List<Product> productListWithoutDuplicates;
    private List<Product> limitedProductListWithoutDuplicates;

    /**
     * Method to setup the dependencies for the test class
     */
    @BeforeEach
    public void setup() {
        recLimitingRecommendationGeneratorService = new RecLimitingRecommendationGeneratorService();
        fill_products_to_product_lists();

    }

    /**
     * Test to verify duplicate remove from the product list.
     */
    @Test
    public void should_return_correct_product_list_after_removing_duplicates() {
        List<Product> resultedProducts = recLimitingRecommendationGeneratorService.generateUniqueRecsForGivenLimit(duplicateProductList,4);
        assertThat(resultedProducts, is(equalTo(productListWithoutDuplicates)));
    }

    /**
     * Test to verify product list limitation by providing the limit.
     */
    @Test
    public void should_return_correct_product_list_after_apply_the_limit() {
        List<Product> resultedProducts = recLimitingRecommendationGeneratorService.generateUniqueRecsForGivenLimit(duplicateProductList,1);
        assertThat(resultedProducts, is(equalTo(limitedProductListWithoutDuplicates)));
    }

    /**
     * Create the products and fill duplicate and non-duplicate lists.
     */
    private void fill_products_to_product_lists() {

        duplicateProductList = new ArrayList<>();
        productListWithoutDuplicates = new ArrayList<>();
        limitedProductListWithoutDuplicates = new ArrayList<>();


        // Create first product.
        Product product1 = new Product();
        product1.getAttributesMap().put("department", "electronics");
        product1.setProductId("1");

        // Create second product.
        Product product2 = new Product();
        product2.getAttributesMap().put("department", "clothing");
        product2.setProductId("2");

        // add created products to the list.
        duplicateProductList.add(product1);
        duplicateProductList.add(product2);
        duplicateProductList.add(product1);

        // add created products to the list.
        productListWithoutDuplicates.add(product1);
        productListWithoutDuplicates.add(product2);

        // add created products to the list.
        limitedProductListWithoutDuplicates.add(product1);
    }
}
