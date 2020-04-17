package com.zone24x7.ibrac.recengine.service;

import com.zone24x7.ibrac.recengine.pojo.*;

import java.util.concurrent.Callable;

public class AlgorithmTask implements Callable<AlgorithmResult> {

    private InputParams inputParams;
    private ActiveBundle activeBundle;
    private RecCycleStatus recCycleStatus;
    private AlgorithmService algorithmService;

    public AlgorithmTask(InputParams inputParams, ActiveBundle activeBundle, RecCycleStatus recCycleStatus, AlgorithmService algorithmService) {
        this.inputParams = inputParams;
        this.activeBundle = activeBundle;
        this.recCycleStatus = recCycleStatus;
        this.algorithmService = algorithmService;
    }

    @Override
    public AlgorithmResult call() {
       return algorithmService.getAlgorithmResult(inputParams, activeBundle, recCycleStatus);
    }
}
