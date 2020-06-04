package com.zone24x7.ibrac.recengine.pipeline.flatrecpipeline.handlers;

import com.zone24x7.ibrac.recengine.enumeration.RecommendationType;
import com.zone24x7.ibrac.recengine.pojo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Class to test AccumulationHandler.
 */
public class AccumulationHandlerTest {
    private AccumulationHandler accumulationHandler;
    private RecInputParams recInputParams;
    private RecStatusParams recStatusParams;
    private ActiveBundle activeBundle;
    private RecCycleStatus recCycleStatus;
    private MultipleAlgorithmResult multipleAlgorithmResult;

    private List<Product> productList = new LinkedList<>();
    private Map<String, String> algoToProductsMap = new HashMap<>();
    private Map<String, String> algoToUsedCcpMap = new HashMap<>();

    /**
     * Setup method.
     */
    @BeforeEach
    public void setup() {
        recInputParams = new RecInputParams();
        recStatusParams = new RecStatusParams();

        activeBundle = mock(ActiveBundle.class);
        recCycleStatus = mock(RecCycleStatus.class);
        multipleAlgorithmResult = mock(MultipleAlgorithmResult.class);

        recInputParams.setPlaceholder("Horizontal");
        recStatusParams.setMultipleAlgorithmResult(multipleAlgorithmResult);
        recStatusParams.setRecCycleStatus(recCycleStatus);

        when(activeBundle.getId()).thenReturn("1");
        when(activeBundle.getLimitToApply()).thenReturn(10);

        when(multipleAlgorithmResult.getRecProducts()).thenReturn(productList);
        when(multipleAlgorithmResult.getDisplayText()).thenReturn("Top Trending Products");
        when(multipleAlgorithmResult.getAlgoToProductsMap()).thenReturn(algoToProductsMap);
        when(multipleAlgorithmResult.getAlgoToUsedCcp()).thenReturn(algoToUsedCcpMap);

        accumulationHandler = new AccumulationHandler();
    }

    /**
     * Test to verify that accumulation handler execution sets the result correctly in status params.
     */
    @Test
    public void should_execute_accumulation_handler_correctly_and_set_result_in_status_params() {
        accumulationHandler.handleTask(recInputParams, recStatusParams, activeBundle);
        RecResult<FlatRecPayload> recResult = recStatusParams.getRecResult();
        assertNotNull(recResult);

        assertThat(recResult.getPlaceHolder(), is(equalTo("Horizontal")));
        assertThat(recResult.getRecCycleStatus(), is(equalTo(recCycleStatus)));

        assertThat(recResult.getRecMetaInfo().getType(), is(equalTo(RecommendationType.FLAT_RECOMMENDATION)));
        assertThat(recResult.getRecMetaInfo().getAlgoToProductsMap(), is(equalTo(algoToProductsMap)));
        assertThat(recResult.getRecMetaInfo().getAlgoToUsedCcp(), is(equalTo(algoToUsedCcpMap)));
        assertThat(recResult.getRecMetaInfo().getBundleId(), is(equalTo("1")));
        assertThat(recResult.getRecMetaInfo().getLimitToApply(), is(equalTo(10)));
        assertThat(recResult.getRecMetaInfo().getExecutedFilteringRuleInfoList().size(), is(equalTo(0)));

        assertThat(recResult.getRecPayload().getProducts(), is(equalTo(productList)));
        assertThat(recResult.getRecPayload().getDisplayText(), is(equalTo("Top Trending Products")));
    }
}