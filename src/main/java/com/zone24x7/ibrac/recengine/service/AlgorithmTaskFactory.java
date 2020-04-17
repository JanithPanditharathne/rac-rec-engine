package com.zone24x7.ibrac.recengine.service;

import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.InputParams;
import org.springframework.beans.factory.annotation.Autowired;

public class AlgorithmTaskFactory {

    @Autowired
    private AlgorithmService algorithmService;

    public AlgorithmTask create(InputParams inputParams, ActiveBundle activeBundle, RecCycleStatus recCycleStatus) {
        return new AlgorithmTask(inputParams, activeBundle, recCycleStatus, algorithmService);
    }
}
