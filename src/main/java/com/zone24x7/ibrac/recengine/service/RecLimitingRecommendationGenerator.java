package com.zone24x7.ibrac.recengine.service;


import com.zone24x7.ibrac.recengine.pojo.Product;

import java.util.List;

/**
 * Interface for the recommendation generator which applies limiting functionalities on the
 * recommendations
 */
public interface RecLimitingRecommendationGenerator {

    /**
     * Generates unique recommendations from the provided list of recommendations and apply the
     * indicated limit on these unique recommendations
     *
     * @param allRecs complete list of recommendations
     * @param limit   the number of unique recommendations required
     * @return the limit applied unique recommended products
     */
    List<Product> generateUniqueRecsForGivenLimit(List<Product> allRecs,
                                                  int limit);

}
