package com.zone24x7.ibrac.recengine.util;

import com.zone24x7.ibrac.recengine.exceptions.MalformedConfigurationException;
import com.zone24x7.ibrac.recengine.pojo.algoparams.AlgoParamInfo;
import com.zone24x7.ibrac.recengine.pojo.algoparams.AlgoParams;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to transform config data.
 */
public final class ConfigDataTransformUtil {
    /**
     * Private constructor
     */
    private ConfigDataTransformUtil() {
        // private constructor
    }

    /**
     * Method to convert a string to the algorithm parameter map.
     *
     * @param algoParamsConfig the algorithm parameter string
     * @return the algorithm parameter map
     * @throws MalformedConfigurationException if configuration is malformed
     */
    public static Map<String, AlgoParams> convertToAlgoParamsMap(String algoParamsConfig) throws MalformedConfigurationException {
        Map<String, AlgoParams> algoParamsMap = new HashMap<>();
        AlgoParamInfo algoParamInfo = null;

        if (StringUtils.isNotEmpty(algoParamsConfig)) {
            try {
                algoParamInfo = JsonPojoConverter.toPojo(algoParamsConfig, AlgoParamInfo.class);
            } catch (IOException e) {
                throw new MalformedConfigurationException("Algo params configuration is malformed.");
            }
        }

        if (algoParamInfo == null || CollectionUtils.isEmpty(algoParamInfo.getAlgorithmParams())) {
            return algoParamsMap;
        }

        for (AlgoParams algoParams : algoParamInfo.getAlgorithmParams()) {
            algoParamsMap.put(algoParams.getAlgoId(), algoParams);
        }

        return algoParamsMap;
    }
}
