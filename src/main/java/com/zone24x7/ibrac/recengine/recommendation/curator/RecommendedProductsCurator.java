package com.zone24x7.ibrac.recengine.recommendation.curator;

import com.zone24x7.ibrac.recengine.pojo.Product;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Interface for the recommended products curator
 */
public interface RecommendedProductsCurator {

    /**
     * Retrieves the curated product of the given product Id.
     *
     * @param productId           Product Id.
     * @param priceFilterDateTime date/time for price filtering.
     * @param recCycleStatus      the recommendation generation cycle status
     * @return Curated product.
     */
    Product getProduct(String productId, ZonedDateTime priceFilterDateTime, RecCycleStatus recCycleStatus);

    /**
     * Retrieves list of curated products of a given list of productIds.
     *
     * @param productIds          list of product Ids to be curated.
     * @param priceFilterDateTime dateTime to get the product with valid price for the given time.
     * @param recCycleStatus      the recommendation generation cycle status.
     * @return list of curated products
     */
    List<Product> getProducts(List<String> productIds, ZonedDateTime priceFilterDateTime, RecCycleStatus recCycleStatus);
}
