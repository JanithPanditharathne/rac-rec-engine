package com.zone24x7.ibrac.recengine.strategy;

import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.InputParams;
import com.zone24x7.ibrac.recengine.pojo.RecResult;

import java.util.concurrent.Callable;

public class PlacementTask implements Callable<RecResult> {

    private RecGenerationEngine recGenerationEngine;
    private InputParams inputParams;
    private RecCycleStatus recCycleStatus;

    public PlacementTask(InputParams inputParams, RecCycleStatus recCycleStatus, RecGenerationEngine recGenerationEngine) {
        this.inputParams = inputParams;
        this.recCycleStatus = recCycleStatus;
        this.recGenerationEngine = recGenerationEngine;
    }

    @Override
    public RecResult call() throws Exception {
        return recGenerationEngine.getResult(inputParams, recCycleStatus);
    }
}
