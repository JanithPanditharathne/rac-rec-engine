package com.zone24x7.ibrac.recengine.strategy;

import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;
import com.zone24x7.ibrac.recengine.pojo.RecResult;

public interface RecGenerationEngine {
    RecResult<?, ?> getResult(RecInputParams recInputParams, RecCycleStatus recCycleStatus);
}
