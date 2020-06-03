package com.zone24x7.ibrac.recengine.service;

import com.zone24x7.ibrac.recengine.exceptions.BaseConnectionException;
import com.zone24x7.ibrac.recengine.pojo.AlgorithmResult;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;

import java.util.Map;

/**
 * Class for producing algorithm result
 */
public interface AlgorithmResultGenerator {
    /**
     * Method to get the an algorithm result for a given algorithm id and channel context parameters.
     *
     * @param algorithmId the algorithm id
     * @param ccp         the channel context parameters
     * @return the generated algorithm result
     * @throws BaseConnectionException base connection exception
     */
    AlgorithmResult getAlgorithmResult(String algorithmId, Map<String, String> ccp, RecCycleStatus recCycleStatus);
}
