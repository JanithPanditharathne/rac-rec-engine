package com.zone24x7.ibrac.recengine.service;

import com.zone24x7.ibrac.recengine.pojo.Product;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Recommendation generator which allows filtering out the unique recommendations from a list of recommendations
 * and providing the specified number of recommendations requested.
 */
@Component
public class RecLimitingRecommendationGeneratorService implements RecLimitingRecommendationGenerator {

    /**
     * Generates unique recommendations from the provided list of recommendations and apply the
     * indicated limit on these unique recommendations
     *
     * @param allRecs complete list of recommendations
     * @param limit   the number of unique recommendations required
     * @return the limit applied unique recommended products
     */
    @Override
    public List<Product> generateUniqueRecsForGivenLimit(List<Product> allRecs, int limit) {
        //First remove duplicates if any
        List<Product> duplicatesRemovedList = removeDuplicates(allRecs);

        //Then apply limiting functionality
        return applyLimit(duplicatesRemovedList, limit);
    }


    /**
     * Method used to remove duplicate products from the final list
     *
     * @param fullProductList full product list that may contain duplicates
     * @return duplicates removed product list
     */
    private static List<Product> removeDuplicates(List<Product> fullProductList) {
        Set<Product> duplicateFilteringSet = new LinkedHashSet<>();
        List<Product> nonDuplicatedProductList = new LinkedList<>();
        duplicateFilteringSet.addAll(fullProductList);
        nonDuplicatedProductList.addAll(duplicateFilteringSet);
        return nonDuplicatedProductList;
    }


    /**
     * Applies the specified limit for the given list of recommendations.
     *
     * @param originalList complete list of recommendations
     * @param limit        the number of recommendations required
     * @return the limit applied product list
     */
    private <T> List<T> applyLimit(List<T> originalList, int limit) {
        if (CollectionUtils.isNotEmpty(originalList) && limit >= 0 && originalList.size() > limit) {
            return originalList.subList(0, limit);
        } else {
            return originalList;
        }
    }

}
