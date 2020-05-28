package com.zone24x7.ibrac.recengine.service;

import com.zone24x7.ibrac.recengine.exceptions.BaseConnectionException;
import com.zone24x7.ibrac.recengine.pojo.AlgorithmResult;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Callable task used to execute algorithms parallel in multiple threads.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AlgorithmTask implements Callable<AlgorithmResult> {
    @Autowired
    private AlgorithmService algorithmService;

    private String algorithmId;
    private Map<String, String> ccp;
    private RecCycleStatus recCycleStatus;

    /**
     * Algorithm task constructor.
     *
     * @param algorithmId    algorithmId.
     * @param ccp            channel context parameters.
     * @param recCycleStatus recCycle status.
     * @return a task to execute.
     **/
    public AlgorithmTask(String algorithmId, Map<String, String> ccp, RecCycleStatus recCycleStatus) {
        this.algorithmId = algorithmId;
        this.ccp = ccp;
        this.recCycleStatus = recCycleStatus;
    }

    /**
     * Overridden call method of the callable task to call the algorithm service.
     *
     * @return Returns an algorithm result.
     */
    @Override
    public AlgorithmResult call() throws Exception {
        return algorithmService.getAlgorithmResult(algorithmId, ccp, recCycleStatus);
    }
}
