package com.zone24x7.ibrac.recengine.pipeline.flatrecpipeline.pojo;

import com.zone24x7.ibrac.recengine.pojo.FlatRecOtherInfo;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.FlatRecPayload;
import com.zone24x7.ibrac.recengine.pojo.RecResult;

public class RecommendationStatusParams {
    private int limit;
    private RecCycleStatus recCycleStatus;
    RecResult<FlatRecPayload, FlatRecOtherInfo> recResult;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public RecCycleStatus getRecCycleStatus() {
        return recCycleStatus;
    }

    public void setRecCycleStatus(RecCycleStatus recCycleStatus) {
        this.recCycleStatus = recCycleStatus;
    }

    public RecResult<FlatRecPayload, FlatRecOtherInfo> getRecResult() {
        return recResult;
    }

    public void setRecResult(RecResult<FlatRecPayload, FlatRecOtherInfo> recResult) {
        this.recResult = recResult;
    }
}
