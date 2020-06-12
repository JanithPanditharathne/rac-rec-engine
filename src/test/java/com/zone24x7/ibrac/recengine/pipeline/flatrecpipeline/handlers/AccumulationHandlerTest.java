package com.zone24x7.ibrac.recengine.pipeline.flatrecpipeline.handlers;

import com.zone24x7.ibrac.recengine.enumeration.RecommendationType;
import com.zone24x7.ibrac.recengine.pojo.*;
import com.zone24x7.ibrac.recengine.pojo.rules.ExecutedRuleInfo;
import com.zone24x7.ibrac.recengine.pojo.rules.FilteringRulesResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.isNull;
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
    private FilteringRulesResult filteringRulesResult;
    private Set<ExecutedRuleInfo> executedRuleInfos;

    private List<Product> productList = new LinkedList<>();
    private List<Product> filteredProductList = new LinkedList<>();
    private Map<String, String> algoToProductsMap = new HashMap<>();
    private Map<String, String> algoToUsedCcpMap = new HashMap<>();

    private Product product1;
    private Product product2;

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
        filteringRulesResult = mock(FilteringRulesResult.class);

        recInputParams.setPlaceholder("Horizontal");
        recStatusParams.setRecCycleStatus(recCycleStatus);

        product1 = new Product();
        product1.setProductId("1");
        product2 = new Product();
        product2.setProductId("2");

        //Populate product list
        productList.add(product1);
        productList.add(product2);

        //Populate filtered product list
        filteredProductList.add(product2);

        ExecutedRuleInfo executedRuleInfo = new ExecutedRuleInfo();
        executedRuleInfos = new HashSet<>();
        executedRuleInfos.add(executedRuleInfo);

        when(activeBundle.getId()).thenReturn("1");
        when(activeBundle.getLimitToApply()).thenReturn(10);

        when(multipleAlgorithmResult.getRecProducts()).thenReturn(productList);
        when(multipleAlgorithmResult.getDisplayText()).thenReturn("Top Trending Products");
        when(multipleAlgorithmResult.getAlgoToProductsMap()).thenReturn(algoToProductsMap);
        when(multipleAlgorithmResult.getAlgoToUsedCcp()).thenReturn(algoToUsedCcpMap);

        when(filteringRulesResult.getFilteredRecommendedProductsList()).thenReturn(filteredProductList);
        when(filteringRulesResult.getExecutedFilteringRuleInfo()).thenReturn(executedRuleInfos);

        accumulationHandler = new AccumulationHandler();
    }

    /**
     * Test to verify that accumulation handler execution sets the result correctly in status params.
     */
    @Test
    public void should_execute_accumulation_handler_correctly_and_set_result_in_status_params() {
        recStatusParams.setMultipleAlgorithmResult(multipleAlgorithmResult);

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
        assertThat(recResult.getRecMetaInfo().getExecutedFilteringRuleInfoList(), is(nullValue()));

        assertThat(recResult.getRecPayload().getProducts(), is(equalTo(productList)));
        assertThat(recResult.getRecPayload().getDisplayText(), is(equalTo("Top Trending Products")));
    }

    /**
     * Test to verify that accumulation handler set rules output product list to rec result when available
     */
    @Test
    public void should_execute_accumulation_handler_correctly_and_set_filtering_handler_rules_output_to_rec_result() {
        recStatusParams.setMultipleAlgorithmResult(multipleAlgorithmResult);
        //Filtering rules result is available
        recStatusParams.setFilteringRulesResult(filteringRulesResult);

        accumulationHandler.handleTask(recInputParams, recStatusParams, activeBundle);
        RecResult<FlatRecPayload> recResult = recStatusParams.getRecResult();
        assertNotNull(recResult);

        //Rules output shoud be set
        assertThat(recResult.getRecPayload().getProducts(), is(equalTo(filteredProductList)));
        assertThat(recResult.getRecMetaInfo().getExecutedFilteringRuleInfoList(), equalTo(executedRuleInfos));

        //Asserting other things
        assertThat(recResult.getPlaceHolder(), is(equalTo("Horizontal")));
        assertThat(recResult.getRecCycleStatus(), is(equalTo(recCycleStatus)));
        assertThat(recResult.getRecMetaInfo().getType(), is(equalTo(RecommendationType.FLAT_RECOMMENDATION)));
        assertThat(recResult.getRecMetaInfo().getAlgoToProductsMap(), is(equalTo(algoToProductsMap)));
        assertThat(recResult.getRecMetaInfo().getAlgoToUsedCcp(), is(equalTo(algoToUsedCcpMap)));
        assertThat(recResult.getRecMetaInfo().getBundleId(), is(equalTo("1")));
        assertThat(recResult.getRecMetaInfo().getLimitToApply(), is(equalTo(10)));

        assertThat(recResult.getRecPayload().getDisplayText(), is(equalTo("Top Trending Products")));
    }
}
