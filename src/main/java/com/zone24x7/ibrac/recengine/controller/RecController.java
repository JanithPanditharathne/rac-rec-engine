package com.zone24x7.ibrac.recengine.controller;

import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.InputParams;
import com.zone24x7.ibrac.recengine.pojo.RecResult;
import com.zone24x7.ibrac.recengine.strategy.PlacementTask;
import com.zone24x7.ibrac.recengine.strategy.PlacementTaskFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;

@RestController
public class RecController {

    @Autowired
    @Qualifier("cachedThreadPoolTaskExecutor")
    private ExecutorService cachedTaskExecutorService;

    @Autowired
    private PlacementTaskFactory placementTaskFactory;

    @RequestMapping("/recengine/v1/recommendations")
    public RecResult getRecommendation() throws Exception {
        InputParams inputParams = new InputParams();
        RecCycleStatus recCycleStatus = new RecCycleStatus();
        PlacementTask placementTask = placementTaskFactory.create(inputParams, recCycleStatus);
        return cachedTaskExecutorService.submit(placementTask).get();
    }
}
