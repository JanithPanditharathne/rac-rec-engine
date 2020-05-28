package com.zone24x7.ibrac.recengine.strategy;

import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;
import com.zone24x7.ibrac.recengine.pojo.RecResult;
import org.springframework.stereotype.Component;

/**
 * Strategy executor to handle unknown type of recommendations.
 */
@Component
public class UnknownStrategyExecutor extends StrategyExecutor {

    /**
     * Method to execute the strategy.
     *
     * @param recInputParams input parameters received.
     * @param activeBundle   active bundle object containing details for algo execution.
     * @param recCycleStatus recCycle status.
     * @return null as there is no known strategy type
     */
    public RecResult execute(RecInputParams recInputParams, ActiveBundle activeBundle, RecCycleStatus recCycleStatus) {
        return null;
    }
}
