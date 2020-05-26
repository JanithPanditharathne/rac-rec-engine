package com.zone24x7.ibrac.recengine.service;


import com.zone24x7.ibrac.recengine.pojo.AlgorithmResult;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;

import java.util.Map;

/**
 * Responsible for executing a single algorithm.
 */
public interface AlgorithmService {
    /**
     * Calls ccp iterators and with the given ccp calls relevant dao class and retrieves the recommendations
     *
     * @param algorithmId    algorithm Id
     * @param ccp            channel context parameter
     * @param recCycleStatus recCycle status.
     * @return result generated from algorithm.
     */
    AlgorithmResult getAlgorithmResult(String algorithmId, Map<String, String> ccp, RecCycleStatus recCycleStatus);
}
