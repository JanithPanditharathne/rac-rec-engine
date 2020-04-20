package com.zone24x7.ibrac.recengine.service;

import com.zone24x7.ibrac.recengine.pojo.RecInputParams;
import com.zone24x7.ibrac.recengine.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AlgorithmTask implements Callable<AlgorithmResult> {
    @Autowired
    private AlgorithmService algorithmService;

    private RecInputParams recInputParams;
    private ActiveBundle activeBundle;
    private RecCycleStatus recCycleStatus;

    public AlgorithmTask(RecInputParams recInputParams, ActiveBundle activeBundle, RecCycleStatus recCycleStatus) {
        this.recInputParams = recInputParams;
        this.activeBundle = activeBundle;
        this.recCycleStatus = recCycleStatus;
    }

    @Override
    public AlgorithmResult call() {
       return algorithmService.getAlgorithmResult(recInputParams, activeBundle, recCycleStatus);
    }
}
