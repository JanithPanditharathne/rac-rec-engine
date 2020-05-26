package com.zone24x7.ibrac.recengine.service;

import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

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
     * @param algorithmId    algorithm id.
     * @param ccp            channel context parameter.
     * @param recCycleStatus recCycle status.
     * @return a Algorithm task is created and returned.
     */
    public AlgorithmTask create(String algorithmId, Map<String, String> ccp, RecCycleStatus recCycleStatus) {
        return context.getBean(AlgorithmTask.class, algorithmId, ccp, recCycleStatus);
    }
}
