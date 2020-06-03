package com.zone24x7.ibrac.recengine.recommendation.curator;

import com.zone24x7.ibrac.recengine.exceptions.DateTimeException;
import com.zone24x7.ibrac.recengine.logging.Log;
import com.zone24x7.ibrac.recengine.pojo.Product;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.RecProductCurationStatus;
import com.zone24x7.ibrac.recengine.util.AppConfigStringConstants;
import com.zone24x7.ibrac.recengine.util.StringConstants;
import com.zone24x7.ibrac.reconlib.api.ProductApi;
import com.zone24x7.ibrac.reconlib.dto.ErrorState;
import com.zone24x7.ibrac.reconlib.dto.Price;
import com.zone24x7.ibrac.reconlib.dto.ReconLibProduct;
import com.zone24x7.ibrac.reconlib.dto.ReconLibProductContainer;
import com.zone24x7.ibrac.reconlib.error.ReconLibException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * ReconLib wrapper implementation of the RecommendedProductsCurator interface
 */
@Component
public class ReconLibRecommendedProductsCurator implements RecommendedProductsCurator {
    @Autowired
    @Qualifier("productApi")
    private ProductApi productApi;

    @Value(AppConfigStringConstants.PRODUCT_CURATE_PRICE_FILTERING_ENABLED)
    private boolean priceFilteringEnabled;

    @Value(AppConfigStringConstants.PRODUCT_CURATE_INVENTORY_FILTERING_ENABLED)
    private boolean inventoryFilteringEnabled;

    @Log
    private Logger logger;

    //Error messages
    private static final String PROD_ID_LIST_NULL_OR_EMPTY = "ProductId list null or empty";
    private static final String PROD_ID_NULL_OR_EMPTY = "ProductId is null or empty";
    private static final String VALID_PRICE_NOT_FOUND = "Valid price not found for productId: {} for DateTime: {}";
    private static final String INVENTORY_NOT_FOUND = "Inventory not found for productId: {}";
    private static final String EXCEPTION_OCCURRED_WHEN_CONVERTING_PRODUCT_PRICE = "Exception occurred when converting product price to product attributes map. Product Id: {} ";
    private static final String EXCEPTION_WHEN_RETRIEVING_PROD_INFO_FOR_PROD_ID = "Exception occurred when trying to retrieve product info from cache for productId: {}";
    private static final String PRODUCT_INFO_NOT_AVAILABLE = "Product info not available for productId: {}";
    private static final String PRODUCT_DETAILS_FOUND = "Product details found for productId: {}";
    private static final String INVALID_DATE_TIME_TO_RETRIEVE_PRODUCTS = "Datetime provided to retrieve product info is null";

    /**
     * Retrieves the curated product of the given product Id.
     *
     * @param productId           Product Id.
     * @param priceFilterDateTime date/time for price filtering.
     * @param recCycleStatus      the recommendation generation cycle status
     * @return Curated product.
     */
    @Override
    public Product getProduct(String productId, ZonedDateTime priceFilterDateTime, RecCycleStatus recCycleStatus) {
        String requestId = recCycleStatus.getRequestId();

        // if the product id is empty, return null.
        if (StringUtils.isEmpty(productId)) {
            logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + PROD_ID_NULL_OR_EMPTY, requestId);
            return null;
        }

        if (priceFilteringEnabled && priceFilterDateTime == null) {
            // if no price is provided for filtering, return null.
            logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + INVALID_DATE_TIME_TO_RETRIEVE_PRODUCTS, requestId);
            return null;
        }

        ZonedDateTime productPriceFilteringDateTime = (priceFilteringEnabled) ? priceFilterDateTime : null;

        // fetch ReconLibProductContainer that contains product, from reconLib api.
        ReconLibProductContainer productContainer = getReconLibProductContainerFromApi(productId, productPriceFilteringDateTime, inventoryFilteringEnabled, requestId);

        // if the result is null due to a reconLib error, return null.
        if (productContainer == null) {
            return null;
        }

        // if the productContainer is not null & has an error, log the error and return null.
        if (productContainer.getErrorState() != null) {
            if (productContainer.getErrorState().equals(ErrorState.NO_INFO)) {
                logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + PRODUCT_INFO_NOT_AVAILABLE, requestId, productId);
            } else if (productContainer.getErrorState().equals(ErrorState.INV_OUT)) {
                logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + INVENTORY_NOT_FOUND, requestId, productId);
            } else if (productContainer.getErrorState().equals(ErrorState.PRICE_OUT)) {
                logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + VALID_PRICE_NOT_FOUND, requestId, productId, productPriceFilteringDateTime);
            }

            return null;
        }

        Product curatedProduct = null;
        // if the product is present without any error, create the curated product.
        if (productContainer.getReconLibProduct() != null) {
            curatedProduct = generateCuratedProduct(productId, productContainer.getReconLibProduct(), requestId);
            logger.debug(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + PRODUCT_DETAILS_FOUND, requestId, productId);
        }

        return curatedProduct;
    }

    /**
     * Retrieves list of curated products of a given list of productIds.
     *
     * @param productIds          list of product Ids to be curated.
     * @param priceFilterDateTime dateTime to get the product with valid price for the given time.
     * @param recCycleStatus      the recommendation generation cycle status.
     * @return list of curated products
     */
    @Override
    public List<Product> getProducts(List<String> productIds, ZonedDateTime priceFilterDateTime, RecCycleStatus recCycleStatus) {
        String requestId = recCycleStatus.getRequestId();

        // if list of products are empty,return.
        if (CollectionUtils.isEmpty(productIds)) {
            logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + PROD_ID_LIST_NULL_OR_EMPTY, requestId);
            return Collections.emptyList();
        }

        if (priceFilteringEnabled && priceFilterDateTime == null) {
            // if no price is provided for filtering, return null.
            logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + INVALID_DATE_TIME_TO_RETRIEVE_PRODUCTS, requestId);
            return Collections.emptyList();
        }

        ZonedDateTime productPriceFilteringDateTime = (priceFilteringEnabled) ? priceFilterDateTime : null;

        List<Product> curatedProducts = new LinkedList<>();
        RecProductCurationStatus recProductCurationStatus = new RecProductCurationStatus();

        for (String productId : productIds) {
            ReconLibProductContainer reconLibProductContainer = getReconLibProductContainerFromApi(productId, productPriceFilteringDateTime, inventoryFilteringEnabled, requestId);
            if (reconLibProductContainer == null) {
                recProductCurationStatus.addErrorOccurredProducts(productId);
                continue;
            }

            Product product = validateAndGetCuratedProduct(productId, reconLibProductContainer, recProductCurationStatus, requestId);

            if (product != null) {
                curatedProducts.add(product);
                // when a product curates successfully, add it to the curated list for logging purposes.
                recProductCurationStatus.addCuratedProduct(productId);
            }
        }

        StringBuilder curationStatusBuilder = new StringBuilder();

        if (CollectionUtils.isNotEmpty(recProductCurationStatus.getErrorOccurredProducts())) {
            curationStatusBuilder.append("Error occurred products: ").append(recProductCurationStatus.getErrorOccurredProducts()).append("\n");
        }

        if (CollectionUtils.isNotEmpty(recProductCurationStatus.getProductInfoNonExistenceProducts())) {
            curationStatusBuilder.append("Product info unavailable products: ").append(recProductCurationStatus.getProductInfoNonExistenceProducts()).append("\n");
        }

        if (CollectionUtils.isNotEmpty(recProductCurationStatus.getProductInventoryNonExistenceProducts())) {
            curationStatusBuilder.append("Out of inventory products: ").append(recProductCurationStatus.getProductInventoryNonExistenceProducts()).append("\n");
        }

        if (CollectionUtils.isNotEmpty(recProductCurationStatus.getProductPriceNonExistenceProducts())) {
            curationStatusBuilder.append("Stale price products: ").append(recProductCurationStatus.getProductPriceNonExistenceProducts()).append("\n");
        }

        String curationStatus = curationStatusBuilder.toString();

        if (StringUtils.isNotEmpty(curationStatus)) {
            logger.info(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Product curation Status: {}", recCycleStatus.getRequestId(), curationStatus);
        }

        String requestedProductIds = String.join(",", productIds);
        String curatedProductIds = String.join(",", recProductCurationStatus.getCuratedProducts());
        logger.info(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Curation Result => " +
                                                                "Number of products to curate: {}, product Ids: {} and Number of products after curation: {}, product Ids: {}",
                                                                requestId,
                                                                productIds.size(),
                                                                requestedProductIds,
                                                                curatedProducts.size(),
                                                                curatedProductIds);

        if(CollectionUtils.isEmpty(curatedProducts) && CollectionUtils.isNotEmpty(productIds)){
            recCycleStatus.indicateCurationRemovedAllProducts();
        }

        return curatedProducts;
    }

    /**
     * Method to get {@link ReconLibProductContainer} from {@link ProductApi}
     *
     * @param productId              id of the product.
     * @param priceFilteringDateTime dateTime for price filtering. if the value is not null, price will be filtered.
     * @param checkInventory         flag for inventory filtering.
     * @param requestId              id of the request
     * @return {@link ReconLibProduct} instance.
     */
    private ReconLibProductContainer getReconLibProductContainerFromApi(String productId, ZonedDateTime priceFilteringDateTime, boolean checkInventory, String requestId) {
        try {
            return productApi.getProduct(productId, priceFilteringDateTime, checkInventory);
        } catch (ReconLibException e) {
            logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + EXCEPTION_WHEN_RETRIEVING_PROD_INFO_FOR_PROD_ID, requestId, productId, e);
        }

        return null;
    }

    /**
     * Method to generate the curated product after validations.
     *
     * @param productId                id of the product.
     * @param productContainer         container received from product api that holds product info.
     * @param recProductCurationStatus {@link RecProductCurationStatus} instance to hold curate status.
     * @param requestId                id of the request.
     * @return Product. Returns a null if the required data is not available.
     */
    private Product validateAndGetCuratedProduct(String productId, ReconLibProductContainer productContainer, RecProductCurationStatus recProductCurationStatus, String requestId) {
        if (productContainer.getErrorState() != null) {
            if (productContainer.getErrorState().equals(ErrorState.NO_INFO)) {
                recProductCurationStatus.addProductInfoNonExistenceProduct(productId);
            } else if (productContainer.getErrorState().equals(ErrorState.INV_OUT)) {
                recProductCurationStatus.addProductInventoryNonExistenceProduct(productId);
            } else if (productContainer.getErrorState().equals(ErrorState.PRICE_OUT)) {
                recProductCurationStatus.addProductPriceNonExistenceProduct(productId);
            }

            return null;
        }

        if (productContainer.getReconLibProduct() != null) {
            return generateCuratedProduct(productId, productContainer.getReconLibProduct(), requestId);
        }

        return null;
    }

    /**
     * Method to generate product with information.
     *
     * @param productId       id of the product.
     * @param reconLibProduct reconLib product.
     * @param requestId       id of the request.
     * @return Product instance.
     */
    private Product generateCuratedProduct(String productId, ReconLibProduct reconLibProduct, String requestId) {
        Product curatedProduct = new Product();
        curatedProduct.setProductId(reconLibProduct.getStaticInfo().get(StringConstants.PRODUCT_ID));
        curatedProduct.getAttributesMap().putAll(reconLibProduct.getStaticInfo());
        curatedProduct.getAttributesMap().put(StringConstants.PRODUCT_IN_INVENTORY, String.valueOf(reconLibProduct.isInInventory()));
        curatedProduct.getAttributesMap().put(StringConstants.PRODUCT_IMG_URL, reconLibProduct.getImageUrl());

        // if product has prices, convert price into an attribute map.
        if (CollectionUtils.isNotEmpty(reconLibProduct.getPrices())) {
            Map<String, String> productPriceAttributesMap = getProductPriceAttributesMap(productId, reconLibProduct.getPrices().get(0), requestId);
            curatedProduct.getAttributesMap().putAll(productPriceAttributesMap);
        }

        return curatedProduct;
    }

    /**
     * Method to convert product {@link Price} attributes to an attribute map.
     *
     * @param productId  id of the product.
     * @param validPrice price of the product.
     * @return map of price attributes.
     */
    private Map<String, String> getProductPriceAttributesMap(String productId, Price validPrice, String requestId) {
        Map<String, String> priceAttributesMap = new HashMap<>();
        //format the regular price to have two digits after decimal point. Eg.: 100.00
        String regularPrice = (validPrice.getRegularPrice() != null) ? String.format("%.2f", validPrice.getRegularPrice()) : null;
        priceAttributesMap.put(StringConstants.PRODUCT_PRICE_REGULAR_PRICE, regularPrice);

        try {
            priceAttributesMap.put(StringConstants.PRODUCT_PRICE_VALID_START_DATE, getFormattedDate(validPrice.getValidStartDate()));
            priceAttributesMap.put(StringConstants.PRODUCT_PRICE_VALID_END_DATE, getFormattedDate(validPrice.getValidEndDate()));
        } catch (Exception e) {
            logger.error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + EXCEPTION_OCCURRED_WHEN_CONVERTING_PRODUCT_PRICE, requestId, productId, e);
        }

        return priceAttributesMap;
    }

    /**
     * Method to format the price dates to a given string format.
     *
     * @param dateTimeToConvert zonedDateTime to convert
     * @return formatted date string.
     */
    private String getFormattedDate(ZonedDateTime dateTimeToConvert) {
        if (dateTimeToConvert == null) {
            return null;
        }

        try {
            return dateTimeToConvert.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss XX"));
        } catch (Exception e) {
            throw new DateTimeException("Error occurred while converting to price datetime", e);
        }
    }
}
