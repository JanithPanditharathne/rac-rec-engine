package com.zone24x7.ibrac.recengine.pipeline;

import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.RecResult;

/**
 * Interface for experience generation strategy.
 */
public interface RecGenerationStrategy<T> {
    /**
     * Method to generate recommendations.
     *
     * @param activeBundle the active bundle containing the configuration to execute the
     *                     strategy.
     * @param inputParams  the input parameters to run the strategy
     * @param recCycleStatus  the object to track the status of recommendation generation
     * @return the generated experience.
     */
    RecResult<?, ?> getRecommendations(ActiveBundle activeBundle, T inputParams, RecCycleStatus recCycleStatus);
}

