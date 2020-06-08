package com.zone24x7.ibrac.recengine.combinationgenerator;

import com.zone24x7.ibrac.recengine.pojo.algoparams.AlgoParams;
import com.zone24x7.ibrac.recengine.util.StringConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Mockito.*;

/**
 * Test class for AlgorithmCombinationGenerator
 */
class AlgorithmCombinationGeneratorTest {
    private Logger logger;
    private AlgorithmCombinationGenerator algorithmCombinationGenerator;
    private AlgoCombinationInputFilter algoCombinationInputFilter;
    private AlgoParams algoParams;
    private Map<String, String> ccpMap;

    private static final String PRIMARY_PRODUCT_TYPE = "primaryProductType";
    private static final String PRODUCT_TYPE = "productType";
    private static final String SUB_PRODUCT_TYPE = "subProductType";
    private static final String GENDER = "gender";
    private static final String BRAND = "brand";
    private static final String ATG_ID = "atgId";
    private static final String MCM_ID = "mcmId";
    private static final String COOKIE_ID = "cookieId";

    private static final String REQUEST_ID = "-1314037956-1973208397";
    private static final String ALGO_PARAMS_NULL_MSG = "Algo params to generate algorithm combinations is null.";
    private static final String EMPTY_CCP_MAP_TO_GENERATE_ALGO_COMBINATIONS_MSG = "Empty ccp map to generate algorithm combinations. Algo Id: {}";
    private static final String MANDATORY_PARAMS_NOT_FOUND_MSG = "Mandatory params not found in ccp map to generate algorithm combinations. AlgoId: {} Ccp Map: {} Required Mandatory Params: {}";
    private static final String CONDITIONAL_MANDATORY_PARAMS_NOT_FOUND_MSG = "Conditional mandatory params not found in ccp map to generate algorithm combinations. AlgoId: {} Ccp Map: {} Required Mandatory Params: {}";
    private static final String NO_COMBINATIONS_GENERATED_MSG = "No algorithm combinations generated. Algo Id: {} Ccp Map: {}";

    /**
     * Setup mock classes
     */
    @BeforeEach
    void setUp() {
        logger = mock(Logger.class);
        algoCombinationInputFilter = mock(AlgoCombinationInputFilter.class);

        algoParams = new AlgoParams();
        algoParams.setAlgoId("869");

        ccpMap = new HashMap<>();
        ccpMap.put(GENDER, "Women's");

        algorithmCombinationGenerator = new AlgorithmCombinationGenerator();
        ReflectionTestUtils.setField(algorithmCombinationGenerator, "logger", logger);
        ReflectionTestUtils.setField(algorithmCombinationGenerator, "algoCombinationInputFilter", algoCombinationInputFilter);
    }

    /**
     * Test to verify an error is logged when algo params is null for the given algo.
     */
    @Test
    public void should_log_an_error_if_the_algo_params_is_null() {
        List<List<String>> result = algorithmCombinationGenerator.generateAlgoCombinations(null, Collections.emptyMap(), REQUEST_ID);

        verify(logger, times(1)).error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + ALGO_PARAMS_NULL_MSG, REQUEST_ID);
        assertThat(result, is(empty()));
    }

    /**
     * Test to verify an error is logged an error is logged if ccp map is empty when generating non generic algo.
     */
    @Test
    public void should_return_an_empty_combination_list_if_the_algo_is_generic() {
        algoParams.setMandatoryParams(new LinkedList<>());
        List<List<String>> result = algorithmCombinationGenerator.generateAlgoCombinations(algoParams, Collections.emptyMap(), REQUEST_ID);
        assertThat(result, is(not(empty())));
        assertThat(result, is(hasSize(1)));
    }

    /**
     * Test to verify an error is logged if the ccp map is empty when generating non generic algo.
     */
    @Test
    public void should_log_an_error_if_the_ccp_map_is_empty_when_generating_a_none_generic_algo() {
        algoParams.setMandatoryParams(Arrays.asList(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE));
        when(algoCombinationInputFilter.getMandatoryParams(eq(algoParams), anyMap())).thenReturn(Collections.emptyList());

        List<List<String>> result = algorithmCombinationGenerator.generateAlgoCombinations(algoParams, Collections.emptyMap(), REQUEST_ID);

        verify(logger, times(1)).error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + EMPTY_CCP_MAP_TO_GENERATE_ALGO_COMBINATIONS_MSG, REQUEST_ID, algoParams.getAlgoId());
        assertThat(result, is(empty()));
    }

    /**
     * Test to verify an empty list is returned if algo params has mandatory but ccp does not contain all mandatory.
     */
    @Test
    public void should_return_empty_combinations_when_algo_params_has_mandatory_and_ccp_does_not_have_all_mandatory_params() {
        algoParams.setMandatoryParams(Arrays.asList(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE));
        when(algoCombinationInputFilter.getMandatoryParams(eq(algoParams), anyMap())).thenReturn(Collections.emptyList());

        List<List<String>> result = algorithmCombinationGenerator.generateAlgoCombinations(algoParams, ccpMap, REQUEST_ID);

        verify(logger, times(1)).error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + MANDATORY_PARAMS_NOT_FOUND_MSG, REQUEST_ID, algoParams.getAlgoId(), ccpMap, algoParams.getMandatoryParams());
        assertThat(result, is(empty()));
    }

    /**
     * Test to verify only mandatory only combinations are returned as expected when algo params has mandatory only.
     */
    @Test
    public void should_return_mandatory_only_combinations_when_algo_params_has_mandatory_only() {
        algoParams.setMandatoryParams(Arrays.asList(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE));
        when(algoCombinationInputFilter.getMandatoryParams(eq(algoParams), anyMap())).thenReturn(Arrays.asList(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE));

        List<List<String>> result = algorithmCombinationGenerator.generateAlgoCombinations(algoParams, ccpMap, REQUEST_ID);

        assertThat(result, hasSize(1));
        assertThat(result.get(0), hasSize(3));
        // check items in order.
        assertThat(result.get(0), contains(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE));
    }

    /**
     * Test to verify an empty list is returned if algo params has conditional mandatory but ccp does not contain conditional mandatory params.
     */
    @Test
    public void should_return_empty_combinations_when_algo_params_has_conditional_mandatory_and_ccp_does_not_have_conditional_mandatory_params() {
        algoParams.setConditionalMandatoryParams(Arrays.asList(Arrays.asList(PRODUCT_TYPE, SUB_PRODUCT_TYPE), Arrays.asList(PRODUCT_TYPE, GENDER)));
        when(algoCombinationInputFilter.getConditionalMandatoryParams(eq(algoParams), anyMap())).thenReturn(Collections.emptyList());

        List<List<String>> result = algorithmCombinationGenerator.generateAlgoCombinations(algoParams, ccpMap, REQUEST_ID);

        verify(logger, times(1)).error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + CONDITIONAL_MANDATORY_PARAMS_NOT_FOUND_MSG, REQUEST_ID, algoParams.getAlgoId(), ccpMap, algoParams.getConditionalMandatoryParams());
        assertThat(result, is(empty()));
    }

    /**
     * Test to verity mandatory + conditional mandatory param combinations are returned, when algo params has mandatory + conditional mandatory params
     */
    @Test
    public void should_return_mandatory_and_conditional_combinations_when_algo_params_has_mandatory_and_conditional_params() {
        algoParams.setMandatoryParams(Collections.singletonList(PRIMARY_PRODUCT_TYPE));
        algoParams.setConditionalMandatoryParams(Arrays.asList(Arrays.asList(PRODUCT_TYPE, SUB_PRODUCT_TYPE), Arrays.asList(PRODUCT_TYPE, GENDER)));

        List<List<String>> returnCombinationList = new LinkedList<>();
        returnCombinationList.add(Arrays.asList(PRODUCT_TYPE, SUB_PRODUCT_TYPE));
        returnCombinationList.add(Arrays.asList(PRODUCT_TYPE, GENDER));

        when(algoCombinationInputFilter.getMandatoryParams(eq(algoParams), anyMap())).thenReturn(Arrays.asList(PRIMARY_PRODUCT_TYPE));
        when(algoCombinationInputFilter.getConditionalMandatoryParams(eq(algoParams), anyMap())).thenReturn(returnCombinationList);

        List<List<String>> result = algorithmCombinationGenerator.generateAlgoCombinations(algoParams, ccpMap, REQUEST_ID);

        // 3 combinations should be available.
        assertThat(result, hasSize(3));
        // mandatory combination
        assertThat(result.get(0), hasSize(1));
        assertThat(result.get(0), contains(PRIMARY_PRODUCT_TYPE));
        // conditional mandatory combination - 1
        assertThat(result.get(1), hasSize(2));
        assertThat(result.get(1), contains(PRODUCT_TYPE, SUB_PRODUCT_TYPE));
        // conditional mandatory combination - 2
        assertThat(result.get(2), hasSize(2));
        assertThat(result.get(2), contains(PRODUCT_TYPE, GENDER));
    }

    /**
     * Test to verity mandatory + combinations disabled optional param combinations are returned, when algo params has mandatory + optional params
     */
    @Test
    public void should_return_mandatory_and_combination_disabled_optional_combinations() {
        algoParams.setMandatoryParams(Collections.singletonList(GENDER));
        algoParams.setOptionalParams(Arrays.asList(ATG_ID, MCM_ID, COOKIE_ID));
        algoParams.setOptionalCombEnabled(false);

        when(algoCombinationInputFilter.getMandatoryParams(eq(algoParams), anyMap())).thenReturn(Arrays.asList(GENDER));
        when(algoCombinationInputFilter.getOptionalParams(eq(algoParams), anyMap())).thenReturn(Arrays.asList(ATG_ID, MCM_ID, COOKIE_ID));

        List<List<String>> result = algorithmCombinationGenerator.generateAlgoCombinations(algoParams, ccpMap, REQUEST_ID);

        // 4 combinations should be available.
        assertThat(result, hasSize(4));
        assertThat(result.get(0), hasSize(2));
        assertThat(result.get(0), contains(GENDER, ATG_ID));
        assertThat(result.get(1), hasSize(2));
        assertThat(result.get(1), contains(GENDER, MCM_ID));
        assertThat(result.get(2), hasSize(2));
        assertThat(result.get(2), contains(GENDER, COOKIE_ID));
        assertThat(result.get(3), hasSize(1));
        assertThat(result.get(3), contains(GENDER));
    }

    /**
     * Test to verity mandatory + combinations enabled optional param combinations are returned, when algo params has mandatory + optional params.
     */
    @Test
    public void should_return_mandatory_and_combination_enabled_optional_combinations() {
        algoParams.setMandatoryParams(Collections.singletonList(GENDER));
        algoParams.setOptionalParams(Arrays.asList(ATG_ID, MCM_ID, COOKIE_ID));
        algoParams.setOptionalCombEnabled(true);

        when(algoCombinationInputFilter.getMandatoryParams(eq(algoParams), anyMap())).thenReturn(Arrays.asList(GENDER));
        when(algoCombinationInputFilter.getOptionalParams(eq(algoParams), anyMap())).thenReturn(Arrays.asList(ATG_ID, MCM_ID, COOKIE_ID));

        List<List<String>> result = algorithmCombinationGenerator.generateAlgoCombinations(algoParams, ccpMap, REQUEST_ID);

        // 8 combinations should be available.
        assertThat(result, hasSize(8));
        assertThat(result.get(0), hasSize(4));
        assertThat(result.get(0), contains(GENDER, ATG_ID, MCM_ID, COOKIE_ID));
        assertThat(result.get(1), contains(GENDER, ATG_ID, MCM_ID));
        assertThat(result.get(2), contains(GENDER, ATG_ID, COOKIE_ID));
        assertThat(result.get(3), contains(GENDER, MCM_ID, COOKIE_ID));
        assertThat(result.get(4), contains(GENDER, ATG_ID));
        assertThat(result.get(5), contains(GENDER, MCM_ID));
        assertThat(result.get(6), contains(GENDER, COOKIE_ID));
        assertThat(result.get(7), contains(GENDER));
    }

    /**
     * Test to verity conditional mandatory only combinations are returned, when algo params has conditional mandatory params only.
     */
    @Test
    public void should_return_conditional_mandatory_only_combinations() {
        List<List<String>> conditionalMandatoryParams = new LinkedList<>();
        conditionalMandatoryParams.add(Arrays.asList(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE));
        conditionalMandatoryParams.add(Arrays.asList(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE));
        conditionalMandatoryParams.add(Collections.singletonList(PRIMARY_PRODUCT_TYPE));
        algoParams.setConditionalMandatoryParams(conditionalMandatoryParams);

        when(algoCombinationInputFilter.getConditionalMandatoryParams(eq(algoParams), anyMap())).thenReturn(conditionalMandatoryParams);

        List<List<String>> result = algorithmCombinationGenerator.generateAlgoCombinations(algoParams, ccpMap, REQUEST_ID);

        assertThat(result, hasSize(3));
        assertThat(result, contains(conditionalMandatoryParams.toArray()));
    }

    /**
     * Test to verity conditional mandatory + combinations disabled optional param combinations are returned, when algo params has conditional mandatory + optional params.
     */
    @Test
    public void should_return_conditional_mandatory_and_combination_disabled_optional_combinations() {
        List<List<String>> conditionalMandatoryParams = new LinkedList<>();
        conditionalMandatoryParams.add(Arrays.asList(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE));
        conditionalMandatoryParams.add(Arrays.asList(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE));
        conditionalMandatoryParams.add(Collections.singletonList(PRIMARY_PRODUCT_TYPE));

        algoParams.setConditionalMandatoryParams(conditionalMandatoryParams);
        algoParams.setOptionalParams(Arrays.asList(ATG_ID, MCM_ID, COOKIE_ID));
        algoParams.setOptionalCombEnabled(false);

        when(algoCombinationInputFilter.getConditionalMandatoryParams(eq(algoParams), anyMap())).thenReturn(conditionalMandatoryParams);
        when(algoCombinationInputFilter.getOptionalParams(eq(algoParams), anyMap())).thenReturn(Arrays.asList(ATG_ID, MCM_ID, COOKIE_ID));

        List<List<String>> result = algorithmCombinationGenerator.generateAlgoCombinations(algoParams, ccpMap, REQUEST_ID);

        // 12 combinations should be available.
        assertThat(result, hasSize(12));
        assertThat(result.get(0), contains(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE, ATG_ID));
        assertThat(result.get(1), contains(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE, MCM_ID));
        assertThat(result.get(2), contains(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE, COOKIE_ID));
        assertThat(result.get(3), contains(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE));

        assertThat(result.get(4), contains(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, ATG_ID));
        assertThat(result.get(5), contains(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, MCM_ID));
        assertThat(result.get(6), contains(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, COOKIE_ID));
        assertThat(result.get(7), contains(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE));

        assertThat(result.get(8), contains(PRIMARY_PRODUCT_TYPE, ATG_ID));
        assertThat(result.get(9), contains(PRIMARY_PRODUCT_TYPE, MCM_ID));
        assertThat(result.get(10), contains(PRIMARY_PRODUCT_TYPE, COOKIE_ID));
        assertThat(result.get(11), contains(PRIMARY_PRODUCT_TYPE));
    }

    /**
     * Test to verity conditional mandatory + combinations enabled optional param combinations are returned, when algo params has conditional mandatory + optional params.
     */
    @Test
    public void should_return_conditional_mandatory_and_combination_enabled_optional_combinations() {
        List<List<String>> conditionalMandatoryParams = new LinkedList<>();
        conditionalMandatoryParams.add(Arrays.asList(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE));
        conditionalMandatoryParams.add(Collections.singletonList(PRIMARY_PRODUCT_TYPE));

        algoParams.setConditionalMandatoryParams(conditionalMandatoryParams);
        algoParams.setOptionalParams(Arrays.asList(ATG_ID, MCM_ID, COOKIE_ID));
        algoParams.setOptionalCombEnabled(true);

        when(algoCombinationInputFilter.getConditionalMandatoryParams(eq(algoParams), anyMap())).thenReturn(conditionalMandatoryParams);
        when(algoCombinationInputFilter.getOptionalParams(eq(algoParams), anyMap())).thenReturn(Arrays.asList(ATG_ID, MCM_ID, COOKIE_ID));

        List<List<String>> result = algorithmCombinationGenerator.generateAlgoCombinations(algoParams, ccpMap, REQUEST_ID);

        // 16 combinations should be available.
        assertThat(result, hasSize(16));
        assertThat(result.get(0), contains(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE, ATG_ID, MCM_ID, COOKIE_ID));
        assertThat(result.get(1), contains(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE, ATG_ID, MCM_ID));
        assertThat(result.get(2), contains(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE, ATG_ID, COOKIE_ID));
        assertThat(result.get(3), contains(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE, MCM_ID, COOKIE_ID));
        assertThat(result.get(4), contains(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE, ATG_ID));
        assertThat(result.get(5), contains(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE, MCM_ID));
        assertThat(result.get(6), contains(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE, COOKIE_ID));
        assertThat(result.get(7), contains(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE));

        assertThat(result.get(8), contains(PRIMARY_PRODUCT_TYPE, ATG_ID, MCM_ID, COOKIE_ID));
        assertThat(result.get(9), contains(PRIMARY_PRODUCT_TYPE, ATG_ID, MCM_ID));
        assertThat(result.get(10), contains(PRIMARY_PRODUCT_TYPE, ATG_ID, COOKIE_ID));
        assertThat(result.get(11), contains(PRIMARY_PRODUCT_TYPE, MCM_ID, COOKIE_ID));
        assertThat(result.get(12), contains(PRIMARY_PRODUCT_TYPE, ATG_ID));
        assertThat(result.get(13), contains(PRIMARY_PRODUCT_TYPE, MCM_ID));
        assertThat(result.get(14), contains(PRIMARY_PRODUCT_TYPE, COOKIE_ID));
        assertThat(result.get(15), contains(PRIMARY_PRODUCT_TYPE));
    }

    /**
     * Test to verify combination disabled optional combinations will be returned as expected.
     */
    @Test
    public void should_generate_combination_disabled_optional_combinations() {
        algoParams.setOptionalParams(Arrays.asList(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE, GENDER, BRAND, ATG_ID, MCM_ID));
        algoParams.setOptionalCombEnabled(false);
        when(algoCombinationInputFilter.getOptionalParams(eq(algoParams), anyMap())).thenReturn(Arrays.asList(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE, GENDER, BRAND, ATG_ID, MCM_ID));

        List<List<String>> result = algorithmCombinationGenerator.generateAlgoCombinations(algoParams, ccpMap, REQUEST_ID);

        assertThat(result, hasSize(7));
        assertThat(result.get(0), contains(PRIMARY_PRODUCT_TYPE));
        assertThat(result.get(1), contains(PRODUCT_TYPE));
        assertThat(result.get(2), contains(SUB_PRODUCT_TYPE));
        assertThat(result.get(3), contains(GENDER));
        assertThat(result.get(4), contains(BRAND));
        assertThat(result.get(5), contains(ATG_ID));
        assertThat(result.get(6), contains(MCM_ID));
    }

    /**
     * Test to verify combination enabled optional combinations will be returned as expected.
     */
    @Test
    public void should_generate_combination_enabled_optional_combinations() {
        algoParams.setOptionalParams(Arrays.asList(PRIMARY_PRODUCT_TYPE, GENDER, MCM_ID));
        algoParams.setOptionalCombEnabled(true);
        when(algoCombinationInputFilter.getOptionalParams(eq(algoParams), anyMap())).thenReturn(Arrays.asList(PRIMARY_PRODUCT_TYPE, GENDER, MCM_ID));

        List<List<String>> result = algorithmCombinationGenerator.generateAlgoCombinations(algoParams, ccpMap, REQUEST_ID);

        assertThat(result, hasSize(7));
        assertThat(result.get(0), contains(PRIMARY_PRODUCT_TYPE, GENDER, MCM_ID));
        assertThat(result.get(1), contains(PRIMARY_PRODUCT_TYPE, GENDER));
        assertThat(result.get(2), contains(PRIMARY_PRODUCT_TYPE, MCM_ID));
        assertThat(result.get(3), contains(GENDER, MCM_ID));
        assertThat(result.get(4), contains(PRIMARY_PRODUCT_TYPE));
        assertThat(result.get(5), contains(GENDER));
        assertThat(result.get(6), contains(MCM_ID));
    }

    /**
     * Test to verify an empty list is returned if optional params are not available in ccp.
     */
    @Test
    public void should_return_an_empty_list_if_optional_combinations_are_not_available_in_ccp() {
        algoParams.setOptionalParams(Arrays.asList(PRIMARY_PRODUCT_TYPE, GENDER, MCM_ID));
        algoParams.setOptionalCombEnabled(true);
        when(algoCombinationInputFilter.getOptionalParams(eq(algoParams), anyMap())).thenReturn(Collections.emptyList());

        List<List<String>> result = algorithmCombinationGenerator.generateAlgoCombinations(algoParams, ccpMap, REQUEST_ID);

        verify(logger, times(1)).error(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + NO_COMBINATIONS_GENERATED_MSG, REQUEST_ID, algoParams.getAlgoId(), ccpMap);
        assertThat(result, is(empty()));
    }
}