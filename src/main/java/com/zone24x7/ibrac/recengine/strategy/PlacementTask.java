package com.zone24x7.ibrac.recengine.strategy;

import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.InputParams;
import com.zone24x7.ibrac.recengine.pojo.RecResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PlacementTask implements Callable<RecResult> {
    @Autowired
    private RecGenerationEngine recGenerationEngine;

    private InputParams inputParams;
    private RecCycleStatus recCycleStatus;

    public PlacementTask(InputParams inputParams, RecCycleStatus recCycleStatus) {
        this.inputParams = inputParams;
        this.recCycleStatus = recCycleStatus;
    }

    @Override
    public RecResult call() throws Exception {
        return recGenerationEngine.getResult(inputParams, recCycleStatus);
    }
}
