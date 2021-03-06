package com.zone24x7.ibrac.recengine.strategy;

import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Factory to create PlacementTask object.
 */
@Component
public class PlacementTaskFactory {
    @Autowired
    private ApplicationContext context;

    /**
     * Creates the PlacementTask object and returns.
     *
     * @param recInputParams input parameters received.
     * @param recCycleStatus recCycle status.
     * @return a Placement task is created and returned.
     */
    public PlacementTask create(RecInputParams recInputParams, RecCycleStatus recCycleStatus) {
        return context.getBean(PlacementTask.class, recInputParams, recCycleStatus);
    }
}
