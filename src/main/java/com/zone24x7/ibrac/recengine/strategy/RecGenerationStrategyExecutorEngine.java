package com.zone24x7.ibrac.recengine.strategy;

import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;
import com.zone24x7.ibrac.recengine.pojo.RecResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Recommendation Generation strategy executor.
 */
@Component
public class RecGenerationStrategyExecutorEngine implements RecGenerationEngine {

    @Autowired
    @Qualifier("strategyExecutors")
    private StrategyExecutor strategyExecutor;

    @Autowired
    private ActiveBundleProvider activeBundleProvider;

    /**
     * Returns the result provided by the implementation strategy based of the active bundle generated.
     *
     * @param recInputParams input parameters received.
     * @param recCycleStatus recCycle status.
     * @return result returned from the strategy.
     */
    public RecResult<?, ?> getResult(RecInputParams recInputParams, RecCycleStatus recCycleStatus) {
        ActiveBundle activeBundle = activeBundleProvider.getActiveBundle(recInputParams);
        return strategyExecutor.execute(recInputParams, activeBundle, recCycleStatus);
    }
}