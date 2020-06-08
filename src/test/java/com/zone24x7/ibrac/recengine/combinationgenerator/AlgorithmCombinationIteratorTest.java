package com.zone24x7.ibrac.recengine.combinationgenerator;

import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.algoparams.AlgoParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for AlgorithmCombinationIterator
 */
class AlgorithmCombinationIteratorTest {
    private AlgorithmCombinationGenerator algorithmCombinationGenerator;
    private AlgorithmCombinationIterator algorithmCombinationIterator;
    private RecCycleStatus recCycleStatus;

    private Map<String, String> ccpMap;
    private AlgoParams algoParams;
    private Map<String, AlgoParams> algoParamsMap;
    private List<AlgorithmCombination> combinationResult;
    private int combinationIterateCount;

    private static final String ALGO_ID = "100";
    private static final String REQUEST_ID = "-1314037956-1973208397";
    private static final String PRIMARY_PRODUCT_TYPE = "primaryProductType";
    private static final String PRODUCT_TYPE = "productType";
    private static final String GENDER = "gender";
    private static final String BRAND = "brand";

    /**
     * Setup method to prepare before running the tests.
     */
    @BeforeEach
    void setUp() {
        algoParams = mock(AlgoParams.class);
        recCycleStatus = mock(RecCycleStatus.class);
        algoParamsMap = mock(Map.class);
        algorithmCombinationGenerator = mock(AlgorithmCombinationGenerator.class);

        ccpMap = new HashMap<>();
        combinationIterateCount = 0;
        combinationResult = new LinkedList<>();

        when(recCycleStatus.getRequestId()).thenReturn(REQUEST_ID);
        when(algoParamsMap.get(ALGO_ID)).thenReturn(algoParams);
    }

    /**
     * Test to verify has more combinations returns false, if no combinations generated.
     */
    @Test
    public void should_return_next_combination_false_if_no_combinations_generated() {
        when(algorithmCombinationGenerator.generateAlgoCombinations(algoParams, ccpMap, REQUEST_ID)).thenReturn(Collections.emptyList());
        initializeAlgorithmCombinationIterator();

        boolean hasNextCombination = algorithmCombinationIterator.hasMoreCombinations();

        assertFalse(hasNextCombination);
    }

    /**
     * Test to verify an empty map is returned if the given algo is a generic where combinations won't apply.
     */
    @Test
    public void should_return_empty_map_as_the_next_combination_if_algo_is_generic() {
        when(algorithmCombinationGenerator.generateAlgoCombinations(algoParams, ccpMap, REQUEST_ID)).thenReturn(Collections.singletonList(Collections.emptyList()));
        initializeAlgorithmCombinationIterator();

        while (algorithmCombinationIterator.hasMoreCombinations()) {
            combinationResult.add(algorithmCombinationIterator.getNextCombination());
            combinationIterateCount++;
        }

        assertThat(combinationResult, hasSize(1));
        assertThat(combinationIterateCount, is(1));
        assertThat(combinationResult.get(0).getCombinationMap(), anEmptyMap());
    }

    /**
     * Test to verify an error is thrown is next combination is requested, when no combinations available.
     */
    @Test
    public void should_throw_an_exception_when_generate_next_combinations_is_accessed_when_no_next_combinations() {
        when(algorithmCombinationGenerator.generateAlgoCombinations(algoParams, ccpMap, REQUEST_ID)).thenReturn(Collections.emptyList());
        initializeAlgorithmCombinationIterator();

        assertThrows(NoSuchElementException.class, () -> {
            algorithmCombinationIterator.getNextCombination();
        });
    }

    /**
     * Test to verify combinations are returned as expected.
     */
    @Test
    public void should_return_combinations_as_expected() {
        ccpMap.put(PRIMARY_PRODUCT_TYPE, "Clothing");
        ccpMap.put(PRODUCT_TYPE, "Women's");
        ccpMap.put(GENDER, "Women's");
        ccpMap.put(BRAND, "Nike");

        List<List<String>> combinationList = new LinkedList<>();
        combinationList.add(Arrays.asList(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, GENDER, BRAND));
        combinationList.add(Arrays.asList(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, GENDER));
        combinationList.add(Arrays.asList(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, BRAND));
        combinationList.add(Arrays.asList(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE));

        Map<String, String> expectedCombinationMap1  = new HashMap<>();
        expectedCombinationMap1.put(PRIMARY_PRODUCT_TYPE, "Clothing");
        expectedCombinationMap1.put(PRODUCT_TYPE, "Women's");
        expectedCombinationMap1.put(GENDER, "Women's");
        expectedCombinationMap1.put(BRAND, "Nike");

        Map<String, String> expectedCombinationMap2  = new HashMap<>();
        expectedCombinationMap2.put(PRIMARY_PRODUCT_TYPE, "Clothing");
        expectedCombinationMap2.put(PRODUCT_TYPE, "Women's");
        expectedCombinationMap2.put(GENDER, "Women's");

        Map<String, String> expectedCombinationMap3  = new HashMap<>();
        expectedCombinationMap3.put(PRIMARY_PRODUCT_TYPE, "Clothing");
        expectedCombinationMap3.put(PRODUCT_TYPE, "Women's");
        expectedCombinationMap3.put(BRAND, "Nike");

        Map<String, String> expectedCombinationMap4  = new HashMap<>();
        expectedCombinationMap4.put(PRIMARY_PRODUCT_TYPE, "Clothing");
        expectedCombinationMap4.put(PRODUCT_TYPE, "Women's");

        when(algorithmCombinationGenerator.generateAlgoCombinations(algoParams, ccpMap, REQUEST_ID)).thenReturn(combinationList);
        initializeAlgorithmCombinationIterator();

        while (algorithmCombinationIterator.hasMoreCombinations()) {
            combinationResult.add(algorithmCombinationIterator.getNextCombination());
            combinationIterateCount++;
        }

        assertThat(combinationResult, hasSize(4));
        assertThat(combinationIterateCount, is(4));
        assertThat(combinationResult.get(0).getCombinationMap(), is(expectedCombinationMap1));
        assertThat(combinationResult.get(1).getCombinationMap(), is(expectedCombinationMap2));
        assertThat(combinationResult.get(2).getCombinationMap(), is(expectedCombinationMap3));
        assertThat(combinationResult.get(3).getCombinationMap(), is(expectedCombinationMap4));
    }

    /**
     * Method to initiate a new algorithm combination iterator.
     */
    private void initializeAlgorithmCombinationIterator() {
        algorithmCombinationIterator = new AlgorithmCombinationIterator(ALGO_ID, ccpMap, recCycleStatus);
        ReflectionTestUtils.setField(algorithmCombinationIterator, "algoParamsMap", algoParamsMap);
        ReflectionTestUtils.setField(algorithmCombinationIterator, "algorithmCombinationGenerator", algorithmCombinationGenerator);
        algorithmCombinationIterator.initialize();
    }
}