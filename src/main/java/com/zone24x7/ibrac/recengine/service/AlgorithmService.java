package com.zone24x7.ibrac.recengine.service;


import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.AlgorithmResult;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;

/**
 * Responsible for executing a single algorithm.
 */
public interface AlgorithmService {
    /**
     * Calls the relevant dao class and will retrieve the recommendations.
     *
     * @param recInputParams input parameters received.
     * @param activeBundle   active bundle object containing details for algo execution.
     * @param recCycleStatus recCycle status.
     * @return result generated from algorithm.
     */
    AlgorithmResult getAlgorithmResult(RecInputParams recInputParams, ActiveBundle activeBundle, RecCycleStatus recCycleStatus);
}
