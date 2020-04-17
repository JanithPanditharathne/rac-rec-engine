package com.zone24x7.ibrac.recengine.service;

import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.AlgorithmResult;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.InputParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Component
public class AlgorithmCombinatorImpl implements AlgorithmCombinator {

    @Autowired
    private AlgorithmTaskFactory algorithmTaskFactory;

    @Autowired
    @Qualifier("cachedThreadPoolTaskExecutor")
    private ExecutorService cachedTaskExecutorService;

    @Override
    public AlgorithmResult getCombinedAlgoResult(InputParams inputParams, ActiveBundle activeBundle, RecCycleStatus recCycleStatus) {
        AlgorithmTask algorithmTask = algorithmTaskFactory.create(inputParams, activeBundle, recCycleStatus);

        Future<AlgorithmResult> task = cachedTaskExecutorService.submit(algorithmTask);
        try {
            return task.get();
        } catch (InterruptedException e) {
           //TODO Log exception
        } catch (ExecutionException e) {
            //TODO Log exception
        }
        return null;
    }
}
