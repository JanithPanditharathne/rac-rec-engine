package com.zone24x7.ibrac.recengine.combinationgenerator;

import com.zone24x7.ibrac.recengine.pojo.algoparams.AlgoParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Test class for AlgoCombinationInputFilter
 */
class AlgoCombinationInputFilterTest {
    private AlgoCombinationInputFilter algoCombinationInputFilter;
    private Map<String, String> ccpMap;
    private AlgoParams algoParams;

    private static final String PRIMARY_PRODUCT_TYPE = "primaryProductType";
    private static final String PRODUCT_TYPE = "productType";
    private static final String SUB_PRODUCT_TYPE = "subProductType";
    private static final String GENDER = "gender";

    private static final String DEPARTMENT = "Clothing";
    private static final String CATEGORY = "Women";
    private static final String SUB_CATEGORY = "Denim";
    private static final String GENDER_VALUE = "Female";

    private static final String ATG_ID = "atgId";
    private static final String MCM_ID = "mcmId";
    private static final String COOKIE_ID = "cookieId";

    private static final String ATG_ID_VALUE = "1234567";
    private static final String MCM_ID_VALUE = "567890";
    private static final String COOKIE_ID_VALUE = "23334567";

    /**
     * Setup method to prepare before running the tests.
     */
    @BeforeEach
    void setUp() {
        algoCombinationInputFilter = new AlgoCombinationInputFilter();
        ccpMap = new HashMap<>();
        algoParams = new AlgoParams();
    }

    /**
     * Test to verify mandatory param combinations will be returned if available all params in ccp.
     */
    @Test
    public void should_return_mandatory_params_if_ccp_contains_all_the_mandatory_params() {
        algoParams.setMandatoryParams(Arrays.asList(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE));

        ccpMap.put(PRIMARY_PRODUCT_TYPE, DEPARTMENT);
        ccpMap.put(PRODUCT_TYPE, CATEGORY);
        ccpMap.put(SUB_PRODUCT_TYPE, SUB_CATEGORY);
        ccpMap.put(GENDER, GENDER_VALUE);

        List<String> combinations = algoCombinationInputFilter.getMandatoryParams(algoParams, ccpMap);

        assertThat(combinations, hasSize(3));
        assertThat(combinations, contains(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE));
    }

    /**
     * Test to verify an empty combination list is returned if ccp does not contain all the mandatory ccp params.
     */
    @Test
    public void should_return_an_empty_combination_list_when_ccp_does_not_contain_all_the_mandatory_params() {
        algoParams.setMandatoryParams(Arrays.asList(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE));

        ccpMap.put(PRIMARY_PRODUCT_TYPE, DEPARTMENT);
        ccpMap.put(PRODUCT_TYPE, CATEGORY);

        List<String> combinations = algoCombinationInputFilter.getMandatoryParams(algoParams, ccpMap);
        assertThat(combinations, is(empty()));
    }

    /**
     * Test to verify an empty combination list is returned when ccp map is empty.
     */
    @Test
    public void should_return_an_an_empty_mandatory_combinations_list_when_ccp_is_empty() {
        algoParams.setMandatoryParams(Arrays.asList(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE));

        List<String> combinations = algoCombinationInputFilter.getMandatoryParams(algoParams, ccpMap);
        assertThat(combinations, is(empty()));
    }

    /**
     * Test to verify an empty combination list is returned when mandatory params is null.
     */
    @Test
    public void should_return_an_empty_mandatory_combinations_list_when_mandatory_params_is_null() {
        ccpMap.put(PRIMARY_PRODUCT_TYPE, DEPARTMENT);
        ccpMap.put(PRODUCT_TYPE, CATEGORY);

        List<String> combinations = algoCombinationInputFilter.getMandatoryParams(algoParams, ccpMap);
        assertThat(combinations, is(empty()));
    }

    /**
     * Test to verify an empty combination list is returned if ccp does not contain conditional mandatory ccp param combinations.
     */
    @Test
    public void should_return_an_empty_list_when_ccp_does_not_contain_any_conditional_mandatory_params() {
        algoParams.setConditionalMandatoryParams(Arrays.asList(Arrays.asList(PRIMARY_PRODUCT_TYPE,
                PRODUCT_TYPE,
                SUB_PRODUCT_TYPE),
                Arrays.asList(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE),
                Collections.singletonList(SUB_PRODUCT_TYPE)));

        ccpMap.put(PRIMARY_PRODUCT_TYPE, DEPARTMENT);
        ccpMap.put(GENDER, GENDER_VALUE);

        List<List<String>> combinations = algoCombinationInputFilter.getConditionalMandatoryParams(algoParams, ccpMap);
        assertThat(combinations, is(empty()));
    }

    /**
     * Test to verify list of ccp available conditional mandatory params will be returned as expected.
     */
    @Test
    public void should_return_only_ccp_available_conditional_mandatory_params() {
        algoParams.setConditionalMandatoryParams(Arrays.asList(Arrays.asList(PRIMARY_PRODUCT_TYPE,
                PRODUCT_TYPE,
                SUB_PRODUCT_TYPE),
                Arrays.asList(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE),
                Collections.singletonList(SUB_PRODUCT_TYPE)));

        ccpMap.put(PRIMARY_PRODUCT_TYPE, DEPARTMENT);
        ccpMap.put(PRODUCT_TYPE, CATEGORY);

        List<List<String>> combinations = algoCombinationInputFilter.getConditionalMandatoryParams(algoParams, ccpMap);

        assertThat(combinations, hasSize(1));
        assertThat(combinations.get(0), contains(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE));
    }

    /**
     * Test to verify an empty conditional mandatory combination list will be returned when ccp map is empty.
     */
    @Test
    public void should_return_an_empty_conditional_mandatory_param_combination_list_when_ccp_is_empty() {
        algoParams.setConditionalMandatoryParams(Arrays.asList(Arrays.asList(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE, SUB_PRODUCT_TYPE),
                Arrays.asList(PRIMARY_PRODUCT_TYPE, PRODUCT_TYPE),
                Collections.singletonList(SUB_PRODUCT_TYPE)));

        List<List<String>> combinations = algoCombinationInputFilter.getConditionalMandatoryParams(algoParams, Collections.emptyMap());
        assertThat(combinations, is(empty()));
    }

    /**
     * Test to verify an empty conditional mandatory combination list will be returned when conditional mandatory param is null.
     */
    @Test
    public void should_return_an_empty_conditional_mandatory_param_combination_list_when_conditional_mandatory_param_is_null() {
        ccpMap.put(PRIMARY_PRODUCT_TYPE, DEPARTMENT);
        ccpMap.put(PRODUCT_TYPE, CATEGORY);

        List<List<String>> combinations = algoCombinationInputFilter.getConditionalMandatoryParams(algoParams, Collections.emptyMap());
        assertThat(combinations, is(empty()));
    }

    /**
     * Test to verify an empty optional combination list will be returned when matching optional params are
     * not available in ccp map.
     */
    @Test
    public void should_return_an_empty_list_if_no_optional_params_available_in_ccp() {
        algoParams.setOptionalParams(Arrays.asList(ATG_ID, MCM_ID, COOKIE_ID));

        ccpMap.put(PRODUCT_TYPE, CATEGORY);

        List<String> combinations = algoCombinationInputFilter.getOptionalParams(algoParams, ccpMap);
        assertThat(combinations, is(empty()));
    }

    /**
     * Test to verify optional combination list will be returned when matching optional params are available in ccp map.
     */
    @Test
    public void should_return_optional_params_if_available_in_ccp() {
        algoParams.setOptionalParams(Arrays.asList(ATG_ID, MCM_ID, COOKIE_ID));

        ccpMap.put(COOKIE_ID, COOKIE_ID_VALUE);
        ccpMap.put(ATG_ID, ATG_ID_VALUE);
        ccpMap.put(MCM_ID, MCM_ID_VALUE);

        List<String> combinations = algoCombinationInputFilter.getOptionalParams(algoParams, ccpMap);

        assertThat(combinations, hasSize(3));
        assertThat(combinations, contains(ATG_ID, MCM_ID, COOKIE_ID));
    }

    /**
     * Test to verify optional combination list will be returned with only matching optional params in ccp map.
     */
    @Test
    public void should_return_only_ccp_available_optional_params() {
        algoParams.setOptionalParams(Arrays.asList(ATG_ID, MCM_ID, COOKIE_ID));
        ccpMap.put(COOKIE_ID, COOKIE_ID_VALUE);
        ccpMap.put(MCM_ID, MCM_ID_VALUE);

        List<String> combinations = algoCombinationInputFilter.getOptionalParams(algoParams, ccpMap);

        assertThat(combinations, hasSize(2));
        assertThat(combinations, contains(MCM_ID, COOKIE_ID));
    }

    /**
     * Test to verify an empty optional combination list will be returned when ccp map is empty.
     */
    @Test
    public void should_return_an_empty_optional_combination_list_when_ccp_is_empty() {
        algoParams.setOptionalParams(Arrays.asList(ATG_ID, MCM_ID, COOKIE_ID));

        List<String> combinations = algoCombinationInputFilter.getOptionalParams(algoParams, Collections.emptyMap());
        assertThat(combinations, is(empty()));
    }

    /**
     * Test to verify an empty optional combination list will be returned when optional params is null.
     */
    @Test
    public void should_return_an_empty_optional_combination_list_when_optional_params_is_null() {
        List<String> combinations = algoCombinationInputFilter.getOptionalParams(algoParams, Collections.emptyMap());
        assertThat(combinations, is(empty()));
    }
}