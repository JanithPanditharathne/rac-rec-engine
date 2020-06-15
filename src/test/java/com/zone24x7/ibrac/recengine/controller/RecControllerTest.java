package com.zone24x7.ibrac.recengine.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zone24x7.ibrac.recengine.exceptions.InputValidationException;
import com.zone24x7.ibrac.recengine.pojo.FlatRecPayload;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;
import com.zone24x7.ibrac.recengine.pojo.RecResult;
import com.zone24x7.ibrac.recengine.pojo.controller.ResponseFormatterConfig;
import com.zone24x7.ibrac.recengine.service.RecLimitingRecommendationGeneratorService;
import com.zone24x7.ibrac.recengine.strategy.PlacementTask;
import com.zone24x7.ibrac.recengine.strategy.PlacementTaskFactory;
import com.zone24x7.ibrac.recengine.util.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Class to test the RecController.
 */
public class RecControllerTest {

    private RecController recController;
    private String channelId = "TCom";
    private String pageId = "PDP";
    private String placementId = "Grid";

    private String invalidPlacementId = "Grid|for";

    private String enCodedCCP = "eyJwcm9kdWN0TnVtYmVyIjoiMjIxMzIyMiJ9";
    private Logger logger;
    private Pattern inputParamValidationPattern;
    private Pattern inputParamValidationPatternExtraChars;
    private String regex = "[a-zA-Z0-9\\\\-\\\\_]+";
    private String charsRegex = "[a-zA-Z0-9\\\\-\\\\_\\\\|\\\\,]+";
    private ChannelContextParamsDecoder channelContextParamsDecoder;
    private PlacementTaskFactory placementTaskFactory;
    private ExecutorService cachedTaskExecutorService;
    Map<String, String> urlDecodedCcpMap;
    private String recResultString = "{\"recPayload\":{\"displayText\":\"Top Trending Products\",\"products\":[{\"productId\":\"1000001\",\"attributesMap\":{\"productTitle\":\"Women's LC Lauren Conrad Eyelet Fit & Flare Dress\",\"productId\":\"1000001\",\"rating\":\"3.0\",\"reviewCount\":\"2\",\"department\":\"Clothing\",\"category\":\"Dresses\",\"productUrl\":\"lauren_conrad_1000001_yellow\",\"brand\":\"Lauren Conrad\",\"gender\":\"Womens\",\"inInventory\":\"true\",\"imageUrl\":\"https://images.pexels.com/photos/1055691/pexels-photo-1055691.jpeg\",\"regularPrice\":\"5500.00\",\"validStartDate\":\"2020/04/20 23:50:30 +0530\",\"validEndDate\":\"2021/05/02 23:50:30 +0530\"}}]},\"recMetaInfo\":{\"type\":\"FLAT_RECOMMENDATION\",\"bundleId\":\"1\",\"limitToApply\":5,\"algoToProductsMap\":{\"100\":\"1000001,1000003,1000006,1000007,1000008,1000009,1000011,1000012,1000001\"},\"algoToUsedCcp\":{\"100\":\"\"},\"executedFilteringRuleInfoList\":[]},\"placeHolder\":\"Grid\"}";
    private RecResult recResult;
    PlacementTask placementTask;
    private static List<String> whitelistedCcpKeys;
    RecLimitingRecommendationGeneratorService recLimitingRecommendationGeneratorService;


    private String currency = "Rs";
    private String imageWidth = "180";
    private String imageHeight = "180";

    /**
     * Method to setup the dependencies for the test class
     */
    @BeforeEach
    public void setup() throws Exception {

        urlDecodedCcpMap = new HashMap<>();
        urlDecodedCcpMap.put("productNumber", "2213222");

        logger = mock(Logger.class);
        placementTaskFactory = mock(PlacementTaskFactory.class);
        cachedTaskExecutorService = mock(ExecutorService.class);
        placementTask = mock(PlacementTask.class);
        recLimitingRecommendationGeneratorService = mock(RecLimitingRecommendationGeneratorService.class);

        recResult = JsonPojoConverter.toPojo(recResultString, new TypeReference<RecResult<FlatRecPayload>>() {
        });

        // Fill the white listed array.
        fill_white_listed_ccp_keys_array();

        // Set the white listed array to CcpProcessorUtilities class.
        CcpProcessorUtilities.setWhitelistedCcpKeys(whitelistedCcpKeys);

        channelContextParamsDecoder = mock(ChannelContextParamsDecoder.class);
        recController = new RecController();

        inputParamValidationPattern = Pattern.compile(regex);
        inputParamValidationPatternExtraChars = Pattern.compile(charsRegex);

        //Get the resulted payload and cast it to flatRecPayload
        FlatRecPayload flatRecPayload = (FlatRecPayload) recResult.getRecPayload();

        Field loggerField = recController.getClass().getDeclaredField("LOGGER");
        CustomReflectionTestUtils.setFinalStaticField(loggerField, this.logger);

        ReflectionTestUtils.setField(recController, "inputParamValidationPattern", inputParamValidationPattern);
        ReflectionTestUtils.setField(recController, "inputParamValidationPatternExtraChars", inputParamValidationPatternExtraChars);
        ReflectionTestUtils.setField(recController, "channelContextParamsDecoder", channelContextParamsDecoder);
        ReflectionTestUtils.setField(recController, "cachedTaskExecutorService", cachedTaskExecutorService);
        ReflectionTestUtils.setField(recController, "placementTaskFactory", placementTaskFactory);
        ReflectionTestUtils.setField(recController, "recLimitingRecommendationGeneratorService", recLimitingRecommendationGeneratorService);

        when(recLimitingRecommendationGeneratorService.generateUniqueRecsForGivenLimit(flatRecPayload.getProducts(), 5)).thenReturn(flatRecPayload.getProducts());
        when(channelContextParamsDecoder.urlDecode(enCodedCCP)).thenReturn(enCodedCCP);
        when(placementTaskFactory.create(ArgumentMatchers.any(RecInputParams.class), ArgumentMatchers.any(RecCycleStatus.class))).thenReturn(placementTask);
    }


    /**
     * Test to verify that the response entry body generate correctly for valid channel,page,placement and ccps.
     */
    @Test
    public void should_generate_response_entry_body_correctly_for_valid_channel_page_placement_and_ccp() throws InterruptedException, ExecutionException, IOException {
        // Mock the RecResult Future.
        Future<RecResult> recResultFuture = mock(Future.class);

        when(channelContextParamsDecoder.deserializeFromBase64StringToMap(enCodedCCP)).thenReturn(urlDecodedCcpMap);
        when(cachedTaskExecutorService.submit(placementTask)).thenReturn(recResultFuture);
        when(recResultFuture.get()).thenReturn(recResult);

        ResponseEntity<Object> responseEntity = recController.getRecommendation(channelId, pageId, placementId, enCodedCCP);

        List<RecResult> recResultList = new ArrayList<>();
        recResultList.add(recResult);

        ObjectNode bodyNodeToCompare = RecResponseFormatter.format(channelId, pageId, recResultList, new ResponseFormatterConfig(null, null, null));

        // Assert the body node.
        assertThat(responseEntity.getBody(), is(Matchers.equalTo(bodyNodeToCompare)));

        verify(placementTaskFactory, times(1)).create(ArgumentMatchers.any(RecInputParams.class), ArgumentMatchers.any(RecCycleStatus.class));
        verify(cachedTaskExecutorService, times(1)).submit(placementTask);
    }

    /**
     * Test to verify that the response entry body generate correctly for valid channel,page,placement and ccps.
     */
    @Test
    public void should_generate_response_entry_header_correctly_for_valid_channel_page_placement_and_ccp() throws InterruptedException, ExecutionException, IOException {
        // Mock the RecResult Future.
        Future<RecResult> recResultFuture = mock(Future.class);

        when(channelContextParamsDecoder.deserializeFromBase64StringToMap(enCodedCCP)).thenReturn(urlDecodedCcpMap);
        when(cachedTaskExecutorService.submit(placementTask)).thenReturn(recResultFuture);
        when(recResultFuture.get()).thenReturn(recResult);

        ResponseEntity<Object> responseEntity = recController.getRecommendation(channelId, pageId, placementId, enCodedCCP);

        List<RecResult> recResultList = new ArrayList<>();

        String node = ((java.util.LinkedList) ((AbstractMap.SimpleImmutableEntry) responseEntity.getHeaders().entrySet().toArray()[0]).getValue()).get(0).toString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode headerNode = mapper.readTree(node);
        String requestId = null;

        recResultList.add(recResult);

        ObjectNode headerNodeToCompare = RecResponseFormatter.formatHeader(channelId, pageId, requestId, recResultList);

        // Assert the header node.
        assertThat(headerNode, is(Matchers.equalTo(headerNodeToCompare)));
    }

    /**
     * Test to verify that empty recommendation response entry body generated when intrrupted exception throws.
     */
    @Test
    public void should_generate_empty_recommendation_response_entry_body_when_interrupted_exception_throws_submit_cached_task_executor_service() throws InterruptedException, ExecutionException, IOException {
        Future<RecResult> recResultFuture = mock(Future.class);

        when(channelContextParamsDecoder.deserializeFromBase64StringToMap(enCodedCCP)).thenReturn(urlDecodedCcpMap);
        when(cachedTaskExecutorService.submit(placementTask)).thenReturn(recResultFuture);
        when(recResultFuture.get()).thenThrow(new InterruptedException()).thenReturn(recResult);

        ResponseEntity<Object> responseEntity = recController.getRecommendation(channelId, pageId, placementId, "");

        List<RecResult> recResultList = new ArrayList<>();
        recResultList.add(recResult);

        ObjectNode nodeToCompare = RecResponseFormatter.format(channelId, pageId, new ArrayList<>(), new ResponseFormatterConfig(null, null, null));

        assertThat(responseEntity.getBody(), is(Matchers.equalTo(nodeToCompare)));
    }


    /**
     * Test to verify that InputValidationException thrown for invalid ccp value format.
     */
    @Test
    public void should_generate_response_entry_body_correctly_without_ccps() throws InterruptedException, ExecutionException, IOException {
        Future<RecResult> recResultFuture = mock(Future.class);

        when(channelContextParamsDecoder.deserializeFromBase64StringToMap(enCodedCCP)).thenReturn(urlDecodedCcpMap);
        when(cachedTaskExecutorService.submit(placementTask)).thenReturn(recResultFuture);
        when(recResultFuture.get()).thenReturn(recResult);

        ResponseEntity<Object> responseEntity = recController.getRecommendation(channelId, pageId, placementId, "");

        List<RecResult> recResultList = new ArrayList<>();
        recResultList.add(recResult);

        ObjectNode nodeToCompare = RecResponseFormatter.format(channelId, pageId, recResultList, new ResponseFormatterConfig(null, null, null));

        assertThat(responseEntity.getBody(), is(Matchers.equalTo(nodeToCompare)));
    }

    /**
     * Test to verify that InputValidationException thrown when channel id is null or empty.
     */
    @Test
    public void should_throw_input_validation_exception_when_channel_id_is_null_or_empty() {

        assertThrows(InputValidationException.class, () -> recController.getRecommendation(null, pageId, placementId, enCodedCCP));
        assertThrows(InputValidationException.class, () -> recController.getRecommendation("", pageId, placementId, enCodedCCP));
    }

    /**
     * Test to verify that InputValidationException thrown when page id is null or empty.
     */
    @Test
    public void should_throw_input_validation_exception_when_page_id_is_null_or_empty() {

        assertThrows(InputValidationException.class, () -> recController.getRecommendation(channelId, null, placementId, enCodedCCP));
        assertThrows(InputValidationException.class, () -> recController.getRecommendation(channelId, "", placementId, enCodedCCP));
    }

    /**
     * Test to verify that InputValidationException thrown when placement id is null or empty.
     */
    @Test
    public void should_throw_input_validation_exception_when_placeholder_id_is_null_or_empty() {

        assertThrows(InputValidationException.class, () -> recController.getRecommendation(channelId, pageId, null, enCodedCCP));
        assertThrows(InputValidationException.class, () -> recController.getRecommendation(channelId, pageId, "", enCodedCCP));
    }


    /**
     * Test to verify that InputValidationException thrown for invalid placement value format.
     */
    @Test
    public void should_throw_input_validation_exception_for_invalid_placement_value_format() throws InterruptedException, ExecutionException, IOException {
        Future<RecResult> recResultFuture = mock(Future.class);

        when(channelContextParamsDecoder.deserializeFromBase64StringToMap(enCodedCCP)).thenReturn(urlDecodedCcpMap);
        when(cachedTaskExecutorService.submit(placementTask)).thenReturn(recResultFuture);
        when(recResultFuture.get()).thenReturn(recResult);

        List<RecResult> recResultList = new ArrayList<>();
        recResultList.add(recResult);

        assertThrows(InputValidationException.class, () -> {
            recController.getRecommendation(channelId, pageId, invalidPlacementId, enCodedCCP);
        });
    }


    /**
     * Test to verify that InputValidationException thrown for invalid ccp value format.
     */
    @Test
    public void should_throw_input_validation_exception_for_invalid_ccp_value_format() throws InterruptedException, ExecutionException, IOException {
        Future<RecResult> recResultFuture = mock(Future.class);

        Map<String, String> urlDecodedCcpMap = null;

        when(channelContextParamsDecoder.deserializeFromBase64StringToMap(enCodedCCP)).thenReturn(urlDecodedCcpMap);
        when(cachedTaskExecutorService.submit(placementTask)).thenReturn(recResultFuture);
        when(recResultFuture.get()).thenReturn(recResult);

        List<RecResult> recResultList = new ArrayList<>();
        recResultList.add(recResult);

        assertThrows(InputValidationException.class, () -> {
            recController.getRecommendation(channelId, pageId, placementId, enCodedCCP);
        });
    }


    /**
     * Fill the whitelistedCcpKeys array list
     */
    private void fill_white_listed_ccp_keys_array() {

        whitelistedCcpKeys = new ArrayList<>();

        whitelistedCcpKeys.add("department");
        whitelistedCcpKeys.add("category");
        whitelistedCcpKeys.add("subCategory");
        whitelistedCcpKeys.add("productNumber");
        whitelistedCcpKeys.add("storeId");
        whitelistedCcpKeys.add("brand");
    }

}
