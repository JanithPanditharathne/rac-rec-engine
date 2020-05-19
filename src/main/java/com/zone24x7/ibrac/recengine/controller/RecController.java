package com.zone24x7.ibrac.recengine.controller;

import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;
import com.zone24x7.ibrac.recengine.pojo.RecResult;
import com.zone24x7.ibrac.recengine.strategy.PlacementTask;
import com.zone24x7.ibrac.recengine.strategy.PlacementTaskFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;

/**
 * Rest Controller class for serving recommendations.
 */
@RestController
public class RecController {
    @Autowired
    @Qualifier("cachedThreadPoolTaskExecutor")
    private ExecutorService cachedTaskExecutorService;

    @Autowired
    private PlacementTaskFactory placementTaskFactory;

    /**
     * Controller method to get recommendations.
     *
     * @return the recommendations json as a http 200 ok response.
     *
     * TODO: Remove the general exception and catch inside the method.
     * @throws Exception if an error occurs
     */
    @GetMapping("/recengine/v1/recommendations")
    public RecResult getRecommendation() throws Exception {
        RecInputParams recInputParams = new RecInputParams();

        // TODO: Set the correct request id
        RecCycleStatus recCycleStatus = new RecCycleStatus("REQUEST_ID");
        PlacementTask placementTask = placementTaskFactory.create(recInputParams, recCycleStatus);
        return cachedTaskExecutorService.submit(placementTask).get();
    }
}
