package com.zone24x7.ibrac.recengine.pipeline;

import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.RecResult;


/**
 * Interface for experience generation strategy.
 */
public interface RecGenerationStrategy<T> {
    /**
     * Generates an experience.
     *
     * @param activeBundle Active bundle containing the configuration to execute the
     *                     strategy.
     * @param inputParams  Input parameters to run the strategy
     * @param recCycleStatus  Object to track the status of recommendation generation
     * @return the generated experience.
     */
    RecResult<?, ?> getRecommendations(ActiveBundle activeBundle, T inputParams, RecCycleStatus recCycleStatus);
}

