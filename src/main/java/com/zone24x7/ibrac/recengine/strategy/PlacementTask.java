package com.zone24x7.ibrac.recengine.strategy;

import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;
import com.zone24x7.ibrac.recengine.pojo.RecResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

/**
 * Recommendation generation task for a given placement.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PlacementTask implements Callable<RecResult> {
    @Autowired
    private RecGenerationEngine recGenerationEngine;

    private RecInputParams recInputParams;
    private RecCycleStatus recCycleStatus;

    /**
     * Constructor for placement task.
     *
     * @param recInputParams input parameters received.
     * @param recCycleStatus recCycle status.recCycleStatus.
     */
    public PlacementTask(RecInputParams recInputParams, RecCycleStatus recCycleStatus) {
        this.recInputParams = recInputParams;
        this.recCycleStatus = recCycleStatus;
    }

    /**
     * Overridden call method of the callable task to call the recommendation engine.
     *
     * @return returns an recommendation result.
     */
    @Override
    public RecResult call() throws Exception {
        return recGenerationEngine.getResult(recInputParams, recCycleStatus);
    }
}
