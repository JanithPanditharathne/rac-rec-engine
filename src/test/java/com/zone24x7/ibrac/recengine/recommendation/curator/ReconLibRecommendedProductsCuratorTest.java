package com.zone24x7.ibrac.recengine.recommendation.curator;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;

import com.zone24x7.ibrac.recengine.exceptions.DateTimeException;
import com.zone24x7.ibrac.recengine.pojo.Product;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.util.StringConstants;
import com.zone24x7.ibrac.reconlib.api.ProductApi;
import com.zone24x7.ibrac.reconlib.dto.ErrorState;
import com.zone24x7.ibrac.reconlib.dto.Price;
import com.zone24x7.ibrac.reconlib.dto.ReconLibProduct;
import com.zone24x7.ibrac.reconlib.dto.ReconLibProductContainer;
import com.zone24x7.ibrac.reconlib.error.ReconLibException;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.collection.IsMapContaining;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.mockito.Mockito.*;

/**
 * Test class of ReconLibRecommendedProductsCurator.
 */
class ReconLibRecommendedProductsCuratorTest {
    private ReconLibRecommendedProductsCurator reconLibRecommendedProductsCurator = new ReconLibRecommendedProductsCurator();

    @Mock
    private Logger logger;
    @Mock
    private ProductApi productApi;
    @Mock
    private RecCycleStatus recCycleStatus;
    @Mock
    private ReconLibException reconLibException;

    private ZonedDateTime currentDateTime;
    private Map<String, String> staticInfoProduct1;
    private Price futureStartDateProductPrice;
    private ReconLibProductContainer reconLibProductContainerWithProduct;

    private ReconLibProductContainer productContainerWithProductInfoNotFound;
    private ReconLibProductContainer productContainerWithInvNotFound;
    private ReconLibProductContainer productContainerWithPricesNotFound;

    private static final String REQUEST_ID = "Req-1233434";
    private static final String PRODUCT_ID_1 = "2213225";
    private static final String PRODUCT_ID_2 = "2213223";
    private static final String TIME_ZONE_TO = "Asia/Colombo";
    private static final String PRODUCT_TITLE_KEY = "productTitle";
    private static final String PRODUCT_TITLE = "Farberware High Performance 2-pc. Nonstick Skillet Set";
    private static final String PRODUCT_RATING_KEY = "rating";
    private static final String PRODUCT_RATING = "3";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss XX");

    //Error Messages
    private static final String PROD_ID_NULL_OR_EMPTY_ERR_MSG = "ProductId is null or empty";
    private static final String PROD_ID_LIST_NULL_OR_EMPTY_ERR_MSG = "ProductId list null or empty";
    private static final String INVALID_DATE_TIME_TO_RETRIEVE_PRODUCTS_ERR_MSG = "Datetime provided to retrieve product info is null";
    private static final String PRODUCT_INFO_NOT_AVAILABLE_ERR_MSG = "Product info not available for productId: {}";
    private static final String EXCEPTION_WHEN_RETRIEVING_PROD_INFO_FOR_PROD_ID_ERR_MSG = "Exception occurred when trying to retrieve product info from cache for productId: {}";
    private static final String VALID_INVENTORY_NOT_FOUND_ERR_MSG = "Inventory not found for productId: {}";
    private static final String VALID_PRICE_NOT_FOUND_ERR_MSG = "Valid price not found for productId: {} for DateTime: {}";
    private static final String OVERALL_CURATION_STATUS_MSG = "Curation Result => Number of products to curate: {}, product Ids: {} and Number of products after curation: {}, product Ids: {}";
    private static final String PRODUCT_DETAILS_FOUND = "Product details found for productId: {}";
    private static final String INVALID_DATE_TIME_TO_RETRIEVE_PRODUCTS = "Datetime provided to retrieve product info is null";

    /**
     * Setup method to prepare the mocked objects before running the tests.
     */
    @BeforeEach
    void setup() throws DateTimeException {
        MockitoAnnotations.initMocks(this);
        when(recCycleStatus.getRequestId()).thenReturn(REQUEST_ID);

        ReflectionTestUtils.setField(reconLibRecommendedProductsCurator, "logger", logger);
        ReflectionTestUtils.setField(reconLibRecommendedProductsCurator, "productApi", productApi);
        ReflectionTestUtils.setField(reconLibRecommendedProductsCurator, "priceFilteringEnabled", false);
        ReflectionTestUtils.setField(reconLibRecommendedProductsCurator, "inventoryFilteringEnabled", false);

        reconLibProductContainerWithProduct = new ReconLibProductContainer();

        productContainerWithProductInfoNotFound = new ReconLibProductContainer();
        productContainerWithProductInfoNotFound.setErrorState(ErrorState.NO_INFO);

        productContainerWithInvNotFound = new ReconLibProductContainer();
        productContainerWithInvNotFound.setErrorState(ErrorState.INV_OUT);

        productContainerWithPricesNotFound = new ReconLibProductContainer();
        productContainerWithPricesNotFound.setErrorState(ErrorState.PRICE_OUT);

        currentDateTime = ZonedDateTime.now();

        //sets a future price
        futureStartDateProductPrice = new Price();
        futureStartDateProductPrice.setRegularPrice(300.00);
        futureStartDateProductPrice.setValidStartDate(ZonedDateTime.now().plusDays(2).withZoneSameInstant(ZoneId.of(TIME_ZONE_TO)));
        futureStartDateProductPrice.setValidEndDate(ZonedDateTime.now().plusDays(3).withZoneSameInstant(ZoneId.of(TIME_ZONE_TO)));

        staticInfoProduct1 = new HashMap<>();
        staticInfoProduct1.put(PRODUCT_TITLE_KEY, PRODUCT_TITLE);
        staticInfoProduct1.put(PRODUCT_RATING_KEY, PRODUCT_RATING);
        staticInfoProduct1.put("productId", PRODUCT_ID_1);
    }

    /**
     * Test to verify that a null is returned if the productId is null or empty.
     */
    @Test
    void should_return_null_if_the_productId_is_null() {
        Product product = reconLibRecommendedProductsCurator.getProduct(null, currentDateTime, recCycleStatus);

        verify(logger, times(1)).error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + PROD_ID_NULL_OR_EMPTY_ERR_MSG, recCycleStatus.getRequestId());
        assertThat(product, is(nullValue()));
    }

    /**
     * Test to verify that a null is returned if the priceFiltering time is null and price filter is enabled.
     */
    @Test
    void should_return_null_if_the_price_filter_is_enabled_and_filter_dateTime_is_null() {
        ReflectionTestUtils.setField(reconLibRecommendedProductsCurator, "priceFilteringEnabled", true);

        Product product = reconLibRecommendedProductsCurator.getProduct(PRODUCT_ID_1, null, recCycleStatus);
        verify(logger, times(1)).error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + INVALID_DATE_TIME_TO_RETRIEVE_PRODUCTS, recCycleStatus.getRequestId());
        assertThat(product, is(nullValue()));
    }

    /**
     * Test to verify that a null is returned if the product api throws an error when fetching the product.
     *
     * @throws ReconLibException if an error occurs.
     */
    @Test
    void should_return_null_if_the_product_api_throws_an_error_when_fetching_the_product() throws ReconLibException {
        when(productApi.getProduct(PRODUCT_ID_1, null, false)).thenThrow(reconLibException);

        Product product = reconLibRecommendedProductsCurator.getProduct(PRODUCT_ID_1, currentDateTime, recCycleStatus);
        verify(logger, times(1)).error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + EXCEPTION_WHEN_RETRIEVING_PROD_INFO_FOR_PROD_ID_ERR_MSG, recCycleStatus.getRequestId(), PRODUCT_ID_1, reconLibException);
        assertThat(product, is(nullValue()));
    }

    /**
     * Test to verify that a null is returned if the product doesn't not have any static info details.
     *
     * @throws ReconLibException if an error occurs.
     */
    @Test
    void should_return_null_if_the_product_does_not_have_static_info_details() throws ReconLibException {
        when(productApi.getProduct(PRODUCT_ID_1, null, false)).thenReturn(productContainerWithProductInfoNotFound);

        Product product = reconLibRecommendedProductsCurator.getProduct(PRODUCT_ID_1, null, recCycleStatus);
        verify(logger, times(1)).error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + PRODUCT_INFO_NOT_AVAILABLE_ERR_MSG, recCycleStatus.getRequestId(), PRODUCT_ID_1);
        assertThat(product, is(nullValue()));
    }

    /**
     * Test to verify that a null is returned if the product does not have inventory.
     *
     * @throws ReconLibException if an exception occurs.
     */
    @Test
    void should_return_a_null_if_the_product_has_no_inventory() throws ReconLibException {
        ReflectionTestUtils.setField(reconLibRecommendedProductsCurator, "inventoryFilteringEnabled", true);
        when(productApi.getProduct(PRODUCT_ID_1, null, true)).thenReturn(productContainerWithInvNotFound);

        Product product = reconLibRecommendedProductsCurator.getProduct(PRODUCT_ID_1, null, recCycleStatus);
        verify(logger, times(1)).error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + VALID_INVENTORY_NOT_FOUND_ERR_MSG, recCycleStatus.getRequestId(), PRODUCT_ID_1);
        assertThat(product, is(nullValue()));
    }

    /**
     * Test to verify that a null is returned if the product does not have prices when price filtering is enabled.
     *
     * @throws ReconLibException if an error occurs.
     */
    @Test
    void should_return_a_null_if_the_product_has_no_prices_when_filtering_is_enabled() throws ReconLibException {
        ReflectionTestUtils.setField(reconLibRecommendedProductsCurator, "priceFilteringEnabled", true);
        ZonedDateTime dateTime = ZonedDateTime.now();
        when(productApi.getProduct(PRODUCT_ID_1, dateTime, false)).thenReturn(productContainerWithPricesNotFound);

        Product product = reconLibRecommendedProductsCurator.getProduct(PRODUCT_ID_1, dateTime, recCycleStatus);
        verify(logger, times(1)).error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + VALID_PRICE_NOT_FOUND_ERR_MSG, recCycleStatus.getRequestId(), PRODUCT_ID_1, dateTime);
        assertThat(product, is(nullValue()));
    }

    /**
     * Test to verify that a product is returned without prices if the product does not have prices.
     *
     * @throws ReconLibException if an error occurs.
     */
    @Test
    void should_return_the_product_without_prices_if_no_prices_available_for_the_product() throws ReconLibException {
        ReconLibProduct reconLibProduct = new ReconLibProduct();
        reconLibProduct.setStaticInfo(staticInfoProduct1);
        reconLibProductContainerWithProduct.setReconLibProduct(reconLibProduct);

        when(productApi.getProduct(PRODUCT_ID_1, null, false)).thenReturn(reconLibProductContainerWithProduct);

        Product product = reconLibRecommendedProductsCurator.getProduct(PRODUCT_ID_1, null, recCycleStatus);

        verify(logger, times(1)).debug(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + PRODUCT_DETAILS_FOUND, recCycleStatus.getRequestId(), PRODUCT_ID_1);
        assertThat(product, is(not(nullValue())));
        assertThat(product.getAttributesMap(), is(aMapWithSize(5)));
    }

    /**
     * Test to verify that a product is returned without price dates if the product does not have start/end dates.
     *
     * @throws ReconLibException if an error occurs.
     */
    @Test
    void should_return_the_product_without_errors_if_product_price_has_no_start_end_dates() throws ReconLibException {
        ReflectionTestUtils.setField(reconLibRecommendedProductsCurator, "inventoryFilteringEnabled", true);

        ReconLibProduct reconLibProduct = new ReconLibProduct();
        Price price = new Price();
        price.setRegularPrice(100.00);
        reconLibProduct.setStaticInfo(staticInfoProduct1);
        reconLibProduct.setPrices(Collections.singletonList(price));
        reconLibProductContainerWithProduct.setReconLibProduct(reconLibProduct);

        when(productApi.getProduct(PRODUCT_ID_1, null, true)).thenReturn(reconLibProductContainerWithProduct);

        Product product = reconLibRecommendedProductsCurator.getProduct(PRODUCT_ID_1, null, recCycleStatus);

        verify(logger, times(1)).debug(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + PRODUCT_DETAILS_FOUND, recCycleStatus.getRequestId(), PRODUCT_ID_1);
        assertThat(product, is(not(nullValue())));
        assertThat(product.getAttributesMap(), is(aMapWithSize(8)));
        assertThat(product.getAttributesMap(), IsMapContaining.hasEntry("validStartDate", null));
        assertThat(product.getAttributesMap(), IsMapContaining.hasEntry("validEndDate", null));
    }

    /**
     * Test to verify that a product is returned without price dates if the product does not have regular price.
     *
     * @throws ReconLibException if an error occurs.
     */
    @Test
    void should_return_the_product_without_errors_if_product_price_has_no_regular_price() throws ReconLibException {
        ReflectionTestUtils.setField(reconLibRecommendedProductsCurator, "inventoryFilteringEnabled", true);

        ReconLibProduct reconLibProduct = new ReconLibProduct();
        Price price = new Price();
        price.setValidStartDate(currentDateTime);
        price.setValidEndDate(currentDateTime);

        reconLibProduct.setStaticInfo(staticInfoProduct1);
        reconLibProduct.setPrices(Collections.singletonList(price));
        reconLibProductContainerWithProduct.setReconLibProduct(reconLibProduct);

        when(productApi.getProduct(PRODUCT_ID_1, null, true)).thenReturn(reconLibProductContainerWithProduct);

        Product product = reconLibRecommendedProductsCurator.getProduct(PRODUCT_ID_1, null, recCycleStatus);

        verify(logger, times(1)).debug(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + PRODUCT_DETAILS_FOUND, recCycleStatus.getRequestId(), PRODUCT_ID_1);
        assertThat(product, is(not(nullValue())));
       // assertThat(product.getAttributesMap(), is(aMapWithSize(8)));
        assertThat(product.getAttributesMap(), IsMapContaining.hasKey("validStartDate"));
        assertThat(product.getAttributesMap(), IsMapContaining.hasKey("validEndDate"));
    }

    /**
     * Test to verify that a product is returned with price when the prices are available.
     *
     * @throws ReconLibException if an error occurs.
     */
    @Test
    void should_return_a_product_with_price_when_prices_are_available() throws ReconLibException {
        ReflectionTestUtils.setField(reconLibRecommendedProductsCurator, "inventoryFilteringEnabled", true);
        ReflectionTestUtils.setField(reconLibRecommendedProductsCurator, "priceFilteringEnabled", true);
        ZonedDateTime zonedDateTime = ZonedDateTime.now();

        String expectedStartDate = DATE_TIME_FORMATTER.format(futureStartDateProductPrice.getValidStartDate());
        String expectedSEndDate = DATE_TIME_FORMATTER.format(futureStartDateProductPrice.getValidEndDate());
        String expectedPrice = "300.00";

        ReconLibProduct reconLibProduct = new ReconLibProduct();
        reconLibProduct.setStaticInfo(staticInfoProduct1);
        reconLibProduct.setInInventory(true);
        reconLibProduct.setPrices(Collections.singletonList(futureStartDateProductPrice));
        reconLibProductContainerWithProduct.setReconLibProduct(reconLibProduct);

        when(productApi.getProduct(PRODUCT_ID_1, zonedDateTime, true)).thenReturn(reconLibProductContainerWithProduct);

        Product product = reconLibRecommendedProductsCurator.getProduct(PRODUCT_ID_1, zonedDateTime, recCycleStatus);
        Map<String, String> attrMap = product.getAttributesMap();

        assertThat(attrMap, hasEntry("validStartDate", expectedStartDate));
        assertThat(attrMap, hasEntry("validEndDate", expectedSEndDate));
        assertThat(attrMap, hasEntry("regularPrice", expectedPrice));
        assertThat(attrMap, hasEntry(PRODUCT_TITLE_KEY, PRODUCT_TITLE));
        assertThat(attrMap, hasEntry(PRODUCT_RATING_KEY, PRODUCT_RATING));
        assertThat(attrMap, hasEntry("inInventory", "true"));
    }

    /**
     * Test to verify that an empty list is returned if the productId list is empty.
     */
    @Test
    void should_return_an_empty_list_if_the_productId_list_is_null() {
        List<Product> productList = reconLibRecommendedProductsCurator.getProducts(null, currentDateTime, recCycleStatus);

        verify(logger, times(1)).error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + PROD_ID_LIST_NULL_OR_EMPTY_ERR_MSG, recCycleStatus.getRequestId());
        assertThat(productList, is(empty()));
    }

    /**
     * Test to verify that an empty list is returned if the product api returns an empty list.
     */
    @Test
    void should_return_an_empty_list_if_the_price_filter_is_enabled_and_priceFilterDate_is_null()  {
        ReflectionTestUtils.setField(reconLibRecommendedProductsCurator, "priceFilteringEnabled", true);
        List<String> productIds = Arrays.asList(PRODUCT_ID_1, PRODUCT_ID_2);

        List<Product> productList = reconLibRecommendedProductsCurator.getProducts(productIds, null, recCycleStatus);
        verify(logger, times(1)).error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + INVALID_DATE_TIME_TO_RETRIEVE_PRODUCTS, REQUEST_ID);
        assertThat(productList, is(empty()));
    }

    /**
     * Test to verify that an empty list is returned if the product api throws an error when fetching the products.
     *
     * @throws ReconLibException if an exception occurs.
     */
    @Test
    void should_return_an_empty_list_if_the_product_api_throws_an_error_when_fetching_the_products() throws ReconLibException {
        when(productApi.getProduct(anyString(), any(), anyBoolean())).thenThrow(reconLibException);
        List<String> productIds = Arrays.asList(PRODUCT_ID_1, PRODUCT_ID_2);

        List<Product> productList = reconLibRecommendedProductsCurator.getProducts(productIds, currentDateTime, recCycleStatus);
        verify(logger, times(1)).error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + EXCEPTION_WHEN_RETRIEVING_PROD_INFO_FOR_PROD_ID_ERR_MSG, recCycleStatus.getRequestId(), PRODUCT_ID_1, reconLibException);
        verify(logger, times(1)).error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + EXCEPTION_WHEN_RETRIEVING_PROD_INFO_FOR_PROD_ID_ERR_MSG, recCycleStatus.getRequestId(), PRODUCT_ID_2, reconLibException);
        verify(logger, times(1)).info(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Product curation Status: {}", REQUEST_ID, "Error occurred products: [2213225, 2213223]\n");
        assertThat(productList, is(empty()));
    }

    /**
     * Test to verify that an empty list is returned if the product static info is null.
     *
     * @throws ReconLibException if an exception occurs.
     */
    @Test
    void should_return_an_empty_list_if_the_product_static_info_is_null() throws ReconLibException {
        List<String> productIds = new LinkedList<>(Arrays.asList(PRODUCT_ID_1, PRODUCT_ID_2));

        when(productApi.getProduct(anyString(), any(), anyBoolean())).thenReturn(productContainerWithProductInfoNotFound);

        List<Product> productList = reconLibRecommendedProductsCurator.getProducts(productIds, currentDateTime, recCycleStatus);
        verify(productApi, times(2)).getProduct(anyString(), any(), anyBoolean());
        verify(logger, times(1)).info(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Product curation Status: {}", REQUEST_ID, "Product info unavailable products: [2213225, 2213223]\n");
        assertThat(productList, is(empty()));
    }

    /**
     * Test to verify that an empty list is returned if the product inventory is unavailable when getting multiple products.
     *
     * @throws ReconLibException if an error occurs.
     */
    @Test
    void should_return_an_empty_list_if_the_products_inventory_is_out() throws ReconLibException {
        List<String> productIds = Arrays.asList(PRODUCT_ID_1, PRODUCT_ID_2);

        when(productApi.getProduct(anyString(), any(), anyBoolean())).thenReturn(productContainerWithInvNotFound);

        List<Product> productList = reconLibRecommendedProductsCurator.getProducts(productIds, currentDateTime, recCycleStatus);
        verify(logger, times(1)).info(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Product curation Status: {}", REQUEST_ID, "Out of inventory products: [2213225, 2213223]\n");
        verify(logger, times(1)).info(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + OVERALL_CURATION_STATUS_MSG, REQUEST_ID, productIds.size(), String.join(",", productIds), 0, StringUtils.EMPTY);
        assertThat(productList, is(empty()));
    }

    /**
     * Test to verify that an empty list is returned if the product prices are empty and the price filtering is enabled.
     *
     * @throws ReconLibException if an error occurs.
     */
    @Test
    void should_return_an_empty_list_if_the_product_prices_are_not_valid_when_price_filter_is_enabled() throws ReconLibException {
        ReflectionTestUtils.setField(reconLibRecommendedProductsCurator, "priceFilteringEnabled", false);

        List<String> productIds = Arrays.asList(PRODUCT_ID_1, PRODUCT_ID_2);
        when(productApi.getProduct(anyString(), any(), anyBoolean())).thenReturn(productContainerWithPricesNotFound);

        List<Product> productList = reconLibRecommendedProductsCurator.getProducts(productIds, currentDateTime, recCycleStatus);
        verify(logger, times(1)).info(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Product curation Status: {}", REQUEST_ID, "Stale price products: [2213225, 2213223]\n");
        verify(logger, times(1)).info(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + OVERALL_CURATION_STATUS_MSG, REQUEST_ID, productIds.size(), String.join(",", productIds), 0, StringUtils.EMPTY);
        assertThat(productList, is(empty()));
    }

    /**
     * Test to verify that an product list is returned even if the product prices are empty when the price filtering is disabled.
     *
     * @throws ReconLibException if an error occurs.
     */
    @Test
    void should_return_list_of_products_as_expected_when_product_price_is_not_available_and_price_filter_is_disabled() throws ReconLibException {
        ReconLibProduct reconLibProduct1 = new ReconLibProduct();
        reconLibProduct1.setInInventory(true);
        reconLibProduct1.setStaticInfo(staticInfoProduct1);
        reconLibProduct1.setImageUrl("sample url");

        reconLibProductContainerWithProduct.setReconLibProduct(reconLibProduct1);
        List<String> productIds = Collections.singletonList(PRODUCT_ID_1);
        when(productApi.getProduct(anyString(), any(), anyBoolean())).thenReturn(reconLibProductContainerWithProduct);

        List<Product> productList = reconLibRecommendedProductsCurator.getProducts(productIds, currentDateTime, recCycleStatus);
        Map<String, String> attrMap = productList.get(0).getAttributesMap();

        verify(logger, times(1)).info(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + OVERALL_CURATION_STATUS_MSG, REQUEST_ID, productIds.size(), String.join(",", productIds), 1, PRODUCT_ID_1);
        assertThat(productList, is(not(empty())));
        assertThat(attrMap, is(aMapWithSize(5)));
    }

    /**
     * Test to verify that an product list is returned even if the product price dates are not available and the price filtering is disabled.
     *
     * @throws ReconLibException if an error occurs.
     */
    @Test
    void should_return_list_of_products_as_expected_when_product_price_is_available_without_dates_and_price_filter_is_disabled() throws ReconLibException {
        ReconLibProduct reconLibProduct1 = new ReconLibProduct();
        reconLibProduct1.setInInventory(true);
        reconLibProduct1.setStaticInfo(staticInfoProduct1);
        reconLibProduct1.setImageUrl("sample url");

        Price price = new Price();
        price.setRegularPrice(500.00);
        reconLibProduct1.getPrices().add(price);

        reconLibProductContainerWithProduct.setReconLibProduct(reconLibProduct1);
        List<String> productIds = Collections.singletonList(PRODUCT_ID_1);
        when(productApi.getProduct(anyString(), any(), anyBoolean())).thenReturn(reconLibProductContainerWithProduct);

        List<Product> productList = reconLibRecommendedProductsCurator.getProducts(productIds, currentDateTime, recCycleStatus);
        Map<String, String> attrMap = productList.get(0).getAttributesMap();

        verify(logger, times(1)).info(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + OVERALL_CURATION_STATUS_MSG, REQUEST_ID, productIds.size(), String.join(",", productIds), 1, PRODUCT_ID_1);
        assertThat(productList, is(not(empty())));
        assertThat(attrMap, is(aMapWithSize(8)));
        assertThat(attrMap, hasEntry("validStartDate", null));
        assertThat(attrMap, hasEntry("validEndDate", null));
        assertThat(attrMap, hasEntry("regularPrice", "500.00"));
    }

    /**
     * Test to verify that a product list is returned as expected.
     *
     * @throws ReconLibException if an error occurs.
     */
    @Test
    void should_return_list_of_products_as_expected() throws ReconLibException {
        ReflectionTestUtils.setField(reconLibRecommendedProductsCurator, "priceFilteringEnabled", true);

        ReconLibProduct reconLibProduct1 = new ReconLibProduct();
        reconLibProduct1.setInInventory(true);
        reconLibProduct1.setStaticInfo(staticInfoProduct1);
        reconLibProduct1.setPrices(Collections.singletonList(futureStartDateProductPrice));
        reconLibProductContainerWithProduct.setReconLibProduct(reconLibProduct1);

        List<String> productIds = Arrays.asList(PRODUCT_ID_1, PRODUCT_ID_2);

        when(productApi.getProduct(anyString(), any(), anyBoolean())).thenReturn(reconLibProductContainerWithProduct, productContainerWithProductInfoNotFound);

        List<Product> productList = reconLibRecommendedProductsCurator.getProducts(productIds, currentDateTime, recCycleStatus);
        verify(logger, times(1)).info(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + OVERALL_CURATION_STATUS_MSG, REQUEST_ID, productIds.size(), String.join(",", productIds), 1, String.join(",", "2213225"));
        verify(logger, times(1)).info(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Product curation Status: {}", REQUEST_ID, "Product info unavailable products: [2213223]\n");
        assertThat(productList, hasSize(1));
    }
}
