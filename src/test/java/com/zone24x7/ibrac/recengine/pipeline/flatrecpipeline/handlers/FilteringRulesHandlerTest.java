package com.zone24x7.ibrac.recengine.pipeline.flatrecpipeline.handlers;

import com.zone24x7.ibrac.recengine.pojo.*;
import com.zone24x7.ibrac.recengine.pojo.rules.FilteringRulesResult;
import com.zone24x7.ibrac.recengine.rules.merchandisingrules.executors.MerchandisingRuleExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.mockito.Mockito.*;

/**
 * Test class for FilteringRulesHandler
 */
class FilteringRulesHandlerTest {

    private FilteringRulesHandler filteringRulesHandler;
    private RecInputParams recInputParams;
    private RecStatusParams recStatusParams;
    private ActiveBundle activeBundle;
    private MultipleAlgorithmResult multipleAlgorithmResult;
    private List<Product> products;
    private Product product1;
    private Set<String> ruleIds;
    private MerchandisingRuleExecutor merchandisingRuleExecutor;
    private RecCycleStatus recCycleStatus;
    private FilteringRulesResult filteringRulesResult;

    /**
     * Setup code for the testing
     */
    @BeforeEach
    void setUp() {
        filteringRulesHandler = new FilteringRulesHandler();

        recInputParams = mock(RecInputParams.class);
        recStatusParams = mock(RecStatusParams.class);
        activeBundle = mock(ActiveBundle.class);
        multipleAlgorithmResult = mock(MultipleAlgorithmResult.class);
        merchandisingRuleExecutor = mock(MerchandisingRuleExecutor.class);
        recCycleStatus = mock(RecCycleStatus.class);
        filteringRulesResult = mock(FilteringRulesResult.class);

        ReflectionTestUtils.setField(filteringRulesHandler, "merchandisingRuleExecutor", merchandisingRuleExecutor);

        products = new LinkedList<>();
        product1 = new Product();
        product1.setProductId("1");

        ruleIds = new HashSet<>();

        when(recInputParams.getCcp()).thenReturn(Collections.emptyMap());
        when(recStatusParams.getRecCycleStatus()).thenReturn(recCycleStatus);
    }

    /**
     * When there are products in the Multiple Algo result and there are rules to run. Merchandising rule executor
     * should be called and the result should be set to recStatusParam
     */
    @Test
    void should_call_the_rules_executor_when_multiple_algo_result_contains_products_and_there_are_rules_to_test() {
        when(recStatusParams.getMultipleAlgorithmResult()).thenReturn(multipleAlgorithmResult);

        products.add(product1);
        when(multipleAlgorithmResult.getRecProducts()).thenReturn(products);

        ruleIds.add("1");
        when(activeBundle.getPlacementFilteringRuleIds()).thenReturn(ruleIds);

        when(merchandisingRuleExecutor.getFilteredRecommendations(products, ruleIds, Collections.emptyMap(), recCycleStatus)).
                thenReturn(filteringRulesResult);

        filteringRulesHandler.handleTask(recInputParams, recStatusParams, activeBundle);

        verify(merchandisingRuleExecutor).getFilteredRecommendations(products, ruleIds, Collections.emptyMap(), recCycleStatus);
        verify(recStatusParams).setFilteringRulesResult(filteringRulesResult);
    }

    /**
     * When there are no products returned no calls should be made
     */
    @Test
    void should_not_call_rule_executor_when_empty_recs_generated() {
        when(recStatusParams.getMultipleAlgorithmResult()).thenReturn(multipleAlgorithmResult);

        //products are empty
        when(multipleAlgorithmResult.getRecProducts()).thenReturn(products);

        ruleIds.add("1");
        when(activeBundle.getPlacementFilteringRuleIds()).thenReturn(ruleIds);

        filteringRulesHandler.handleTask(recInputParams, recStatusParams, activeBundle);

        verify(merchandisingRuleExecutor, never()).getFilteredRecommendations(products, ruleIds, Collections.emptyMap(), recCycleStatus);
        verify(recStatusParams, never()).setFilteringRulesResult(filteringRulesResult);
    }

    /**
     * When there are no rules to run no rule executor call should be made
     */
    @Test
    void should_not_call_rule_executor_when_empty_rules_to_run() {
        when(recStatusParams.getMultipleAlgorithmResult()).thenReturn(multipleAlgorithmResult);

        products.add(product1);
        when(multipleAlgorithmResult.getRecProducts()).thenReturn(products);

        //Empty rule set
        when(activeBundle.getPlacementFilteringRuleIds()).thenReturn(ruleIds);

        filteringRulesHandler.handleTask(recInputParams, recStatusParams, activeBundle);

        verify(merchandisingRuleExecutor, never()).getFilteredRecommendations(products, ruleIds, Collections.emptyMap(), recCycleStatus);
        verify(recStatusParams, never()).setFilteringRulesResult(filteringRulesResult);
    }
}
