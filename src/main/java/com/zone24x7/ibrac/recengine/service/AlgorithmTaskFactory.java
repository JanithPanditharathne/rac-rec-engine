package com.zone24x7.ibrac.recengine.service;

import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Factory to create AlgorithmTask object.
 */
@Component
public class AlgorithmTaskFactory {

    @Autowired
    private ApplicationContext context;

    /**
     * Creates the AlgorithmTask object and returns.
     *
     * @param recInputParams input parameters received.
     * @param activeBundle   active bundle object containing details for algo execution.
     * @param recCycleStatus recCycle status.
     * @return a Algorithm task is created and returned.
     */
    public AlgorithmTask create(RecInputParams recInputParams, ActiveBundle activeBundle, RecCycleStatus recCycleStatus) {
        return context.getBean(AlgorithmTask.class, recInputParams, activeBundle, recCycleStatus);
    }
}
