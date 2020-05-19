package com.zone24x7.ibrac.recengine.util;

import com.zone24x7.ibrac.recengine.exceptions.MalformedConfigurationException;
import com.zone24x7.ibrac.recengine.pojo.algoparams.AlgoParams;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Class to test the ConfigDataTransformUtil.
 */
public class ConfigDataTransformUtilTest {
    private static final String VALID_ALGO_PARAM_JSON = "{\"algorithmParams\":[{\"algoId\":\"100\",\"algoName\":\"Algo 100\",\"optionalCombEnabled\":false,\"localization\":false,\"localizationParams\":[],\"mandatoryParams\":[\"department\",\"category\",\"subcategory\"],\"conditionalMandatoryParams\":[],\"optionalParams\":[\"brand\",\"age\"]},{\"algoId\":\"200\",\"algoName\":\"Algo 200\",\"optionalCombEnabled\":false,\"localization\":false,\"localizationParams\":[],\"mandatoryParams\":[],\"conditionalMandatoryParams\":[[\"department\",\"category\",\"subcategory\"],[\"department\",\"category\"],[\"department\"]],\"optionalParams\":[]},{\"algoId\":\"300\",\"algoName\":\"Algo 300\",\"optionalCombEnabled\":false,\"localization\":false,\"localizationParams\":[],\"mandatoryParams\":[],\"conditionalMandatoryParams\":[],\"optionalParams\":[\"brand\",\"gender\",\"age\"]}]}";
    private static final String INVALID_JSON = "Invalid Json";
    private static final String INVALID_FORMAT_ALGO_PARAM_JSON = "{\"algoId\":\"100\",\"algoName\":\"Algo 100\",\"optionalCombEnabled\":false,\"localization\":false,\"localizationParams\":[],\"mandatoryParams\":[\"department\",\"category\",\"subcategory\"],\"conditionalMandatoryParams\":[],\"optionalParams\":[\"brand\",\"age\"]},{\"algoId\":\"200\",\"algoName\":\"Algo 200\",\"optionalCombEnabled\":false,\"localization\":false,\"localizationParams\":[],\"mandatoryParams\":[],\"conditionalMandatoryParams\":[[\"department\",\"category\",\"subcategory\"],[\"department\",\"category\"],[\"department\"]],\"optionalParams\":[]},{\"algoId\":\"300\",\"algoName\":\"Algo 300\",\"optionalCombEnabled\":false,\"localization\":false,\"localizationParams\":[],\"mandatoryParams\":[],\"conditionalMandatoryParams\":[],\"optionalParams\":[\"brand\",\"gender\",\"age\"]}";

    /**
     * Test to verify that empty map is returned when input config json is empty.
     *
     * @throws MalformedConfigurationException if the configuration is malformed
     */
    @Test
    public void should_return_empty_map_when_input_config_json_is_empty() throws MalformedConfigurationException {
        Map<String, AlgoParams> algoParamsMap = ConfigDataTransformUtil.convertToAlgoParamsMap("");
        assertThat(algoParamsMap.isEmpty(), is(equalTo(true)));
    }

    /**
     * Test to verify that empty map is returned when input config is null.
     *
     * @throws MalformedConfigurationException if the configuration is malformed
     */
    @Test
    public void should_return_empty_map_when_input_config_json_is_null() throws MalformedConfigurationException {
        Map<String, AlgoParams> algoParamsMap = ConfigDataTransformUtil.convertToAlgoParamsMap(null);
        assertThat(algoParamsMap.isEmpty(), is(equalTo(true)));
    }

    /**
     * Test to verify that malformed configuration exception is thrown when json is invalid.
     *
     * @throws MalformedConfigurationException if the configuration is malformed
     */
    @Test
    public void should_throw_malformed_configuration_exception_when_json_is_invalid() {
        assertThrows(MalformedConfigurationException.class, () -> ConfigDataTransformUtil.convertToAlgoParamsMap(INVALID_JSON));
    }

    /**
     * Test to verify that empty map is returned when input config json is in invalid format.
     *
     * @throws MalformedConfigurationException if the configuration is malformed
     */
    @Test
    public void should_return_empty_map_when_input_config_json_is_in_invalid_format() throws MalformedConfigurationException {
        Map<String, AlgoParams> algoParamsMap = ConfigDataTransformUtil.convertToAlgoParamsMap(INVALID_FORMAT_ALGO_PARAM_JSON);
        assertThat(algoParamsMap.isEmpty(), is(equalTo(true)));
    }

    /**
     * Test to verify that algo param map is generated correctly when a valid json is passed.
     *
     * @throws MalformedConfigurationException if the configuration is malformed
     */
    @Test
    public void should_return_algo_param_map_correctly_when_valid_json_is_passed() throws MalformedConfigurationException {
        Map<String, AlgoParams> algoParamsMap = ConfigDataTransformUtil.convertToAlgoParamsMap(VALID_ALGO_PARAM_JSON);
        assertThat(algoParamsMap.size(), is(equalTo(3)));
        assertThat(algoParamsMap.get("100").getAlgoId(), is(equalTo("100")));
        assertThat(algoParamsMap.get("200").getAlgoId(), is(equalTo("200")));
        assertThat(algoParamsMap.get("300").getAlgoId(), is(equalTo("300")));
    }
}