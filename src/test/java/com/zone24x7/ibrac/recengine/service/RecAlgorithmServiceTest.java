package com.zone24x7.ibrac.recengine.service;

import com.zone24x7.ibrac.recengine.combinationgenerator.AlgoCombinationIteratorProvider;
import com.zone24x7.ibrac.recengine.combinationgenerator.AlgorithmCombination;
import com.zone24x7.ibrac.recengine.combinationgenerator.AlgorithmCombinationIterator;
import com.zone24x7.ibrac.recengine.combinationgenerator.CombinationIterator;
import com.zone24x7.ibrac.recengine.exceptions.BaseConnectionException;
import com.zone24x7.ibrac.recengine.pojo.AlgorithmResult;
import com.zone24x7.ibrac.recengine.pojo.Product;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.util.CustomReflectionTestUtils;
import com.zone24x7.ibrac.recengine.util.StringConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

/**
 * Test class of RecAlgorithmService
 */
class RecAlgorithmServiceTest {
    private Logger logger;
    private AlgorithmResultGenerator algorithmResultGenerator;
    private AlgoCombinationIteratorProvider algoCombinationIteratorProvider;
    private AlgorithmService recAlgorithmService;
    private RecCycleStatus recCycleStatus;
    private Map<String, String> ccpMap;
    private AlgorithmCombination algorithmCombination;
    private CombinationIterator<AlgorithmCombination> algorithmCombinationIterator;
    private Map<String, String> combinationMap1;
    private Map<String, String> combinationMap2;

    private static final String ALGO_ID = "100";
    private static final String REQUEST_ID = "-1314037956-1973208397";

    /**
     * Method to setup the dependencies for the test class
     */
    @BeforeEach
    public void setup() throws Exception {
        algorithmResultGenerator = mock(AlgorithmResultGenerator.class);
        algoCombinationIteratorProvider = mock(AlgoCombinationIteratorProvider.class);
        logger = mock(Logger.class);
        recCycleStatus = mock(RecCycleStatus.class);
        ccpMap = Collections.emptyMap();
        algorithmCombinationIterator = mock(AlgorithmCombinationIterator.class);

        algorithmCombination = mock(AlgorithmCombination.class);
        recAlgorithmService = new RecAlgorithmService();

        combinationMap1 = new HashMap<>();
        combinationMap1.put("department", "Clothing");

        combinationMap2 = new HashMap<>();
        combinationMap2.put("department", "Electronics");
        combinationMap2.put("category", "Home Automation");

        Field loggerField = recAlgorithmService.getClass().getDeclaredField("LOGGER");
        CustomReflectionTestUtils.setFinalStaticField(loggerField, this.logger);

        ReflectionTestUtils.setField(recAlgorithmService, "algorithmResultGenerator", algorithmResultGenerator);
        ReflectionTestUtils.setField(recAlgorithmService, "algoCombinationIteratorProvider", algoCombinationIteratorProvider);

        when(recCycleStatus.getRequestId()).thenReturn(REQUEST_ID);
    }

    /**
     * Test to verify an empty result is returned when algo combinations are not found.
     *
     * @throws BaseConnectionException if an error occurs.
     */
    @Test
    public void should_return_an_empty_algorithm_result_if_no_algo_combinations_found() throws BaseConnectionException {
        when(algoCombinationIteratorProvider.getCombinationIterator(ALGO_ID, ccpMap, recCycleStatus)).thenReturn(algorithmCombinationIterator);
        when(algorithmCombinationIterator.hasMoreCombinations()).thenReturn(false);

        AlgorithmResult algorithmResult = recAlgorithmService.getAlgorithmResult(ALGO_ID, ccpMap, recCycleStatus);

        assertNull(algorithmResult.getAlgorithmId());
        assertThat(algorithmResult.getRecProducts(), empty());
        assertThat(algorithmResult.getUsedCcp(), anEmptyMap());
    }

    /**
     * Test to verify algo combinations are continued until algo result products are found.
     *
     * @throws BaseConnectionException if an error occurs.
     */
    @Test
    public void should_iterate_algorithm_combinations_until_results_are_found() throws BaseConnectionException {
        List<Product> productList = new LinkedList<>();
        productList.add(new Product());
        productList.add(new Product());

        AlgorithmResult algorithmResultWithoutProducts = new AlgorithmResult();
        algorithmResultWithoutProducts.setAlgorithmId(ALGO_ID);
        algorithmResultWithoutProducts.setUsedCcp(ccpMap);

        AlgorithmResult algorithmResultWithProducts = new AlgorithmResult();
        algorithmResultWithProducts.setAlgorithmId(ALGO_ID);
        algorithmResultWithProducts.setUsedCcp(ccpMap);
        algorithmResultWithProducts.setRecProducts(productList);

        when(algoCombinationIteratorProvider.getCombinationIterator(ALGO_ID, ccpMap, recCycleStatus)).thenReturn(algorithmCombinationIterator);
        when(algorithmCombinationIterator.hasMoreCombinations()).thenReturn(true, true, true);
        when(algorithmCombinationIterator.getNextCombination()).thenReturn(algorithmCombination);
        when(algorithmCombination.getCombinationMap()).thenReturn(combinationMap2, combinationMap1);
        when(algorithmResultGenerator.getAlgorithmResult(eq(ALGO_ID), anyMap(), eq(recCycleStatus))).thenReturn(algorithmResultWithoutProducts, algorithmResultWithProducts);

        AlgorithmResult algorithmResult = recAlgorithmService.getAlgorithmResult(ALGO_ID, ccpMap, recCycleStatus);

        verify(algorithmCombinationIterator, times(2)).hasMoreCombinations();
        verify(algorithmCombinationIterator, times(2)).getNextCombination();
        verify(algorithmResultGenerator, times(2)).getAlgorithmResult(eq(ALGO_ID), anyMap(), eq(recCycleStatus));
        verify(logger, times(1)).info(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "Results produced for. AlgoId: {}, ccp: {}", recCycleStatus.getRequestId(), ALGO_ID, combinationMap1);

        verify(logger, times(1)).info(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "No Results produced for. AlgoId: {}, ccps: {}",
                recCycleStatus.getRequestId(), ALGO_ID, combinationMap2.toString());

        assertThat(algorithmResult.getRecProducts(), hasSize(2));
    }

    /**
     * Test to verify all the not available combinations are logged as expected.
     *
     * @throws BaseConnectionException if an error occurs.
     */
    @Test
    public void should_log_not_found_combinations_as_expected() throws BaseConnectionException {
        AlgorithmResult algorithmResultWithoutProducts = new AlgorithmResult();
        algorithmResultWithoutProducts.setAlgorithmId(ALGO_ID);
        algorithmResultWithoutProducts.setUsedCcp(ccpMap);

        when(algoCombinationIteratorProvider.getCombinationIterator(ALGO_ID, ccpMap, recCycleStatus)).thenReturn(algorithmCombinationIterator);
        when(algorithmCombinationIterator.hasMoreCombinations()).thenReturn(true, true, false);
        when(algorithmCombinationIterator.getNextCombination()).thenReturn(algorithmCombination);
        when(algorithmCombination.getCombinationMap()).thenReturn(combinationMap2, combinationMap1);
        when(algorithmResultGenerator.getAlgorithmResult(eq(ALGO_ID), anyMap(), eq(recCycleStatus))).thenReturn(algorithmResultWithoutProducts);

        AlgorithmResult algorithmResult = recAlgorithmService.getAlgorithmResult(ALGO_ID, ccpMap, recCycleStatus);

        verify(algorithmCombinationIterator, times(3)).hasMoreCombinations();
        verify(algorithmCombinationIterator, times(2)).getNextCombination();
        verify(algorithmResultGenerator, times(2)).getAlgorithmResult(eq(ALGO_ID), anyMap(), eq(recCycleStatus));

        verify(logger, times(1)).info(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "No Results produced for. AlgoId: {}, ccps: {}",
                recCycleStatus.getRequestId(), ALGO_ID, combinationMap2.toString() + combinationMap1.toString());

        assertThat(algorithmResult.getRecProducts(), empty());
    }
}