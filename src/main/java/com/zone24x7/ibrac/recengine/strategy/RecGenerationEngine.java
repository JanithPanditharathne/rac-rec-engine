package com.zone24x7.ibrac.recengine.strategy;

import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.InputParams;
import com.zone24x7.ibrac.recengine.pojo.RecResult;

public interface RecGenerationEngine {
    RecResult<?, ?> getResult(InputParams inputParams, RecCycleStatus recCycleStatus);
}
