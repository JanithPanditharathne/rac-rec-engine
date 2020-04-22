package com.zone24x7.ibrac.recengine.strategy;

import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;
import com.zone24x7.ibrac.recengine.pojo.RecResult;

/**
 * Interface for multiple types strategies to execute.
 */
public interface RecGenerationEngine {
    /**
     * Returns the result provided by the implementation strategy.
     *
     * @param recInputParams input parameters received.
     * @param recCycleStatus recCycle status.
     * @return result returned from the strategy.
     */
    RecResult<?, ?> getResult(RecInputParams recInputParams, RecCycleStatus recCycleStatus);
}
