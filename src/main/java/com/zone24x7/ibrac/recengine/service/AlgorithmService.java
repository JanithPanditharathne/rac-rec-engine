package com.zone24x7.ibrac.recengine.service;


import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.AlgorithmResult;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.InputParams;

public interface AlgorithmService {
    AlgorithmResult getAlgorithmResult(InputParams inputParams, ActiveBundle activeBundle, RecCycleStatus recCycleStatus);
}
