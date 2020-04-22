package com.zone24x7.ibrac.recengine.service;

import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.AlgorithmResult;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;

/**
 * Exposes methods to combine results from multiple algorithms according to the provided strategy.
 */
public interface AlgorithmCombinator {
    /**
     * Combines results from multiple algorithms.
     *
     * @param recInputParams input parameters received.
     * @param activeBundle   active bundle object containing details for algo execution.
     * @param recCycleStatus recCycle status.
     * @return a combined algorithm Result.
     */
    AlgorithmResult getCombinedAlgoResult(RecInputParams recInputParams, ActiveBundle activeBundle, RecCycleStatus recCycleStatus);
}
