package com.zone24x7.ibrac.recengine.service;

import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.AlgorithmResult;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

/**
 * Callable task used to execute algorithms parallel in multiple threads.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AlgorithmTask implements Callable<AlgorithmResult> {
    @Autowired
    private AlgorithmService algorithmService;

    private RecInputParams recInputParams;
    private ActiveBundle activeBundle;
    private RecCycleStatus recCycleStatus;

    /**
     * Algorithm task constructor.
     *
     * @param recInputParams input parameters received.
     * @param activeBundle   active bundle object containing details for algorithm execution.
     * @param recCycleStatus recCycle status.
     * @return a task to execute.
     **/
    public AlgorithmTask(RecInputParams recInputParams, ActiveBundle activeBundle, RecCycleStatus recCycleStatus) {
        this.recInputParams = recInputParams;
        this.activeBundle = activeBundle;
        this.recCycleStatus = recCycleStatus;
    }

    /**
     * Overridden call method of the callable task to call the algorithm service.
     *
     * @return Returns an algorithm result.
     */
    @Override
    public AlgorithmResult call() {
        return algorithmService.getAlgorithmResult(recInputParams, activeBundle, recCycleStatus);
    }
}
