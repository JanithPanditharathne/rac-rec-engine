package com.zone24x7.ibrac.recengine.strategy;

import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.InputParams;
import org.springframework.beans.factory.annotation.Autowired;

public class PlacementTaskFactory {
    @Autowired
    private RecGenerationEngine recGenerationEngine;

    public PlacementTask create(InputParams inputParams, RecCycleStatus recCycleStatus){
        return new PlacementTask(inputParams, recCycleStatus, recGenerationEngine);
    }
}
