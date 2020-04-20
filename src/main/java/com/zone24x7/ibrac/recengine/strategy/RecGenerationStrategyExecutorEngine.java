package com.zone24x7.ibrac.recengine.strategy;

import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.InputParams;
import com.zone24x7.ibrac.recengine.pojo.RecResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class RecGenerationStrategyExecutorEngine implements RecGenerationEngine {

    @Autowired
    @Qualifier("strategyExecutors")
    private StrategyExecutor strategyExecutor;

    @Autowired
    private ActiveBundleProvider activeBundleProvider;

    public RecResult<?, ?> getResult(InputParams inputParams, RecCycleStatus recCycleStatus) {
        ActiveBundle activeBundle = activeBundleProvider.getActiveBundle(inputParams);
        return strategyExecutor.execute(inputParams, activeBundle, recCycleStatus);
    }
}