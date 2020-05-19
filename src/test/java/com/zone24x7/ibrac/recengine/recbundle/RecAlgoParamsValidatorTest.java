package com.zone24x7.ibrac.recengine.recbundle;

import com.zone24x7.ibrac.recengine.exceptions.MalformedConfigurationException;
import com.zone24x7.ibrac.recengine.pojo.algoparams.AlgoParams;
import com.zone24x7.ibrac.recengine.util.ConfigDataTransformUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Class to test RecAlgoParamsValidator.
 */
public class RecAlgoParamsValidatorTest {
    private static final String VALID_JSON_STRING = "{\"algorithmParams\":[{\"algoId\":\"100\",\"algoName\":\"Algo 100\",\"optionalCombEnabled\":false,\"localization\":false,\"localizationParams\":[],\"mandatoryParams\":[\"department\",\"category\",\"subcategory\"],\"conditionalMandatoryParams\":[],\"optionalParams\":[\"brand\",\"age\"]},{\"algoId\":\"200\",\"algoName\":\"Algo 200\",\"optionalCombEnabled\":false,\"localization\":false,\"localizationParams\":[],\"mandatoryParams\":[],\"conditionalMandatoryParams\":[[\"department\",\"category\",\"subcategory\"],[\"department\",\"category\"],[\"department\"]],\"optionalParams\":[]},{\"algoId\":\"300\",\"algoName\":\"Algo 300\",\"optionalCombEnabled\":false,\"localization\":false,\"localizationParams\":[],\"mandatoryParams\":[],\"conditionalMandatoryParams\":[],\"optionalParams\":[\"brand\",\"gender\",\"age\"]}]}";
    private RecAlgoParamsValidator recAlgoParamsValidator;

    /**
     * Setup method.
     *
     * @throws MalformedConfigurationException if configuration is malformed.
     */
    @BeforeEach
    public void setup() throws MalformedConfigurationException {
        recAlgoParamsValidator = new RecAlgoParamsValidator();
        Map<String, AlgoParams> algoParamsMap = ConfigDataTransformUtil.convertToAlgoParamsMap(VALID_JSON_STRING);
        ReflectionTestUtils.setField(recAlgoParamsValidator, "algoParamsMap", algoParamsMap);
    }

    /**
     * Test to verify that true is returned when all mandatory parameters are available.
     */
    @Test
    public void should_return_true_when_all_mandatory_parameters_are_available() {
        Map<String, String> ccp = new HashMap<>();
        ccp.put("department", "shoes");
        ccp.put("category", "sports shoes");
        ccp.put("subcategory", "sport new");
        ccp.put("brand", "Adidas");

        boolean validity = recAlgoParamsValidator.isValidForIncomingContext("100", ccp);
        assertThat(validity, is(equalTo(true)));
    }

    /**
     * Test to verify that false is returned when a mandatory parameter is missing.
     */
    @Test
    public void should_return_false_when_a_mandatory_parameter_is_missing() {
        Map<String, String> ccp = new HashMap<>();
        ccp.put("category", "sports shoes");
        ccp.put("subcategory", "sport new");

        boolean validity = recAlgoParamsValidator.isValidForIncomingContext("100", ccp);
        assertThat(validity, is(equalTo(false)));
    }

    /**
     * Test to verify that false is returned when ccp is empty.
     */
    @Test
    public void should_return_false_when_ccp_is_empty() {
        Map<String, String> ccp = new HashMap<>();

        boolean validity = recAlgoParamsValidator.isValidForIncomingContext("100", ccp);
        assertThat(validity, is(equalTo(false)));
    }

    /**
     * Test to verify that true is returned when all conditional mandatory parameter are available.
     */
    @Test
    public void should_return_true_when_all_conditional_mandatory_parameters_are_available_for_all_combinations() {
        Map<String, String> ccp = new HashMap<>();
        ccp.put("department", "shoes");
        ccp.put("category", "sports shoes");
        ccp.put("subcategory", "sport new");
        ccp.put("brand", "Adidas");

        boolean validity = recAlgoParamsValidator.isValidForIncomingContext("200", ccp);
        assertThat(validity, is(equalTo(true)));
    }

    /**
     * Test to verify that true is returned true when all conditional mandatory parameters are available for some combinations.
     */
    @Test
    public void should_return_true_when_all_conditional_mandatory_parameters_are_available_for_some_combinations() {
        Map<String, String> ccp = new HashMap<>();
        ccp.put("department", "shoes");
        ccp.put("category", "sports shoes");

        boolean validity = recAlgoParamsValidator.isValidForIncomingContext("200", ccp);
        assertThat(validity, is(equalTo(true)));
    }

    /**
     * Test to verify that true is returned when all conditional mandatory parameters are available from one combination.
     */
    @Test
    public void should_return_true_when_all_conditional_mandatory_parameters_are_available_for_one_combinations() {
        Map<String, String> ccp = new HashMap<>();
        ccp.put("department", "shoes");

        boolean validity = recAlgoParamsValidator.isValidForIncomingContext("200", ccp);
        assertThat(validity, is(equalTo(true)));
    }

    /**
     * Test to verify that false is returned is returned when all conditional mandatory parameters are not available.
     */
    @Test
    public void should_return_false_when_all_conditional_mandatory_parameters_are_not_available() {
        Map<String, String> ccp = new HashMap<>();
        ccp.put("brand", "Nike");

        boolean validity = recAlgoParamsValidator.isValidForIncomingContext("200", ccp);
        assertThat(validity, is(equalTo(false)));
    }

    /**
     * Test to verify that false is returned when a conditional parameter set has insufficient parameters.
     */
    @Test
    public void should_return_false_when_a_conditional_mandatory_parameter_set_has_insufficient_parameters() {
        Map<String, String> ccp = new HashMap<>();
        ccp.put("category", "sports shoes");

        boolean validity = recAlgoParamsValidator.isValidForIncomingContext("200", ccp);
        assertThat(validity, is(equalTo(false)));
    }

    /**
     * Test to verify that false is returned in a conditional mandatory parameter algorithm when received ccp is empty.
     */
    @Test
    public void should_return_false_in_a_conditional_mandatory_parameter_algo_with_received_empty_ccp() {
        Map<String, String> ccp = new HashMap<>();

        boolean validity = recAlgoParamsValidator.isValidForIncomingContext("200", ccp);
        assertThat(validity, is(equalTo(false)));
    }

    /**
     * Test to verify that true is returned when all optional parameters are available.
     */
    @Test
    public void should_return_true_when_all_optional_parameters_are_available() {
        Map<String, String> ccp = new HashMap<>();
        ccp.put("brand", "nike");
        ccp.put("gender", "male");
        ccp.put("age", "adult");

        boolean validity = recAlgoParamsValidator.isValidForIncomingContext("300", ccp);
        assertThat(validity, is(equalTo(true)));
    }

    /**
     * Test to verify that true is returned some optional parameters are available.
     */
    @Test
    public void should_return_true_when_some_optional_parameters_are_available() {
        Map<String, String> ccp = new HashMap<>();
        ccp.put("brand", "nike");
        ccp.put("age", "adult");

        boolean validity = recAlgoParamsValidator.isValidForIncomingContext("300", ccp);
        assertThat(validity, is(equalTo(true)));
    }

    /**
     * Test to verify that true is returned when no optional parameters are available.
     */
    @Test
    public void should_return_true_when_no_optional_parameters_are_available() {
        Map<String, String> ccp = new HashMap<>();
        ccp.put("department", "shoes");

        boolean validity = recAlgoParamsValidator.isValidForIncomingContext("300", ccp);
        assertThat(validity, is(equalTo(true)));
    }

    /**
     * Test to verify that true is returned in optional parameter only algo when ccp is empty.
     */
    @Test
    public void should_return_true_when_in_optional_parameter_only_algo_when_ccp_is_empty() {
        Map<String, String> ccp = new HashMap<>();

        boolean validity = recAlgoParamsValidator.isValidForIncomingContext("300", ccp);
        assertThat(validity, is(equalTo(true)));
    }

    /**
     * Test to verify that false is returned when invalid algo id is passed.
     */
    @Test
    public void should_return_false_when_invalid_algo_id_passed() {
        Map<String, String> ccp = new HashMap<>();
        ccp.put("department", "shoes");

        boolean validity = recAlgoParamsValidator.isValidForIncomingContext("800", ccp);
        assertThat(validity, is(equalTo(false)));
    }

    /**
     * Test to verify that map is generated correctly when configurations are set.
     */
    @Test
    public void should_generate_the_map_correctly_when_configurations_are_set() {
        Map<String, AlgoParams> algoParamsMap = (Map<String, AlgoParams>) ReflectionTestUtils.getField(recAlgoParamsValidator, "algoParamsMap");

        assertThat(algoParamsMap.size(), is(equalTo(3)));
        assertThat(algoParamsMap.get("100").getAlgoId(), is(equalTo("100")));
        assertThat(algoParamsMap.get("200").getAlgoId(), is(equalTo("200")));
        assertThat(algoParamsMap.get("300").getAlgoId(), is(equalTo("300")));
    }

    /**
     * Test to verify that false is returned when algo map is empty.
     */
    @Test
    public void should_return_false_when_algo_params_map_is_empty() {
        RecAlgoParamsValidator recAlgoParamsValidator1 = new RecAlgoParamsValidator();
        Map<String, AlgoParams> algoParamsMap = new HashMap<>();
        ReflectionTestUtils.setField(recAlgoParamsValidator1, "algoParamsMap", algoParamsMap);

        Map<String, String> ccp = new HashMap<>();
        ccp.put("department", "shoes");
        boolean validity = recAlgoParamsValidator1.isValidForIncomingContext("100", ccp);
        assertThat(validity, is(equalTo(false)));
    }
}