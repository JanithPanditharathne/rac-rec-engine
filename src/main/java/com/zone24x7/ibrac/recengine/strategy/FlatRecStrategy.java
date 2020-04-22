package com.zone24x7.ibrac.recengine.strategy;

import com.zone24x7.ibrac.recengine.pipeline.flatrecpipeline.FlatRecOnlyIncludedRecGenerationStrategy;
import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;
import com.zone24x7.ibrac.recengine.pojo.RecResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Strategy to return flat type of recommendations.
 */
@Component
public class FlatRecStrategy extends StrategyExecutor {

    @Autowired
    private FlatRecOnlyIncludedRecGenerationStrategy flatRecOnlyIncludedRecGenerationStrategy;

    /**
     * Calls the flat rec strategy if the parameters satisfy the conditions.
     *
     * @param recInputParams input parameters received.
     * @param activeBundle   active bundle object containing details for algo execution.
     * @param recCycleStatus recCycle status.
     * @return a flat recommendation result.
     */
    public RecResult execute(RecInputParams recInputParams, ActiveBundle activeBundle, RecCycleStatus recCycleStatus) {
        if ("flat".equalsIgnoreCase(activeBundle.getType())) {
            return flatRecOnlyIncludedRecGenerationStrategy.getRecommendations(activeBundle, recInputParams, recCycleStatus);
        }
        return nextExecutor.execute(recInputParams, activeBundle, recCycleStatus);
    }
}
