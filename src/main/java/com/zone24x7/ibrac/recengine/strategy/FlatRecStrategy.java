package com.zone24x7.ibrac.recengine.strategy;

import com.zone24x7.ibrac.recengine.pipeline.flatrecpipeline.FlatRecOnlyIncludedRecGenerationStrategy;
import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.InputParams;
import com.zone24x7.ibrac.recengine.pojo.RecResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FlatRecStrategy extends StrategyExecutor {

    @Autowired
    private FlatRecOnlyIncludedRecGenerationStrategy flatRecOnlyIncludedRecGenerationStrategy;

    public RecResult execute(InputParams inputParams, ActiveBundle activeBundle, RecCycleStatus recCycleStatus) {
        if ("flat".equalsIgnoreCase(activeBundle.getType())) {
            return flatRecOnlyIncludedRecGenerationStrategy.getRecommendations(activeBundle, inputParams, recCycleStatus);
        }
        return nextExecutor.execute(inputParams, activeBundle, recCycleStatus);
    }
}
