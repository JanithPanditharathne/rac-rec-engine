package com.zone24x7.ibrac.recengine.pipeline.flatrecpipeline.handlers;

import com.zone24x7.ibrac.recengine.pojo.FlatRecOtherInfo;
import com.zone24x7.ibrac.recengine.pipeline.flatrecpipeline.pojo.RecommendationStatusParams;
import com.zone24x7.ibrac.recengine.pojo.*;
import com.zone24x7.ibrac.recengine.service.AlgorithmCombinator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
@Qualifier("coreRecommendationHandler")
public class CoreRecommendationHandler extends RecUnitHandler {

    @Autowired
    private AlgorithmCombinator algorithmCombinator;

    @Override
    public void handleTask(InputParams recInputParams, RecommendationStatusParams recStatusParams, ActiveBundle activeBundle) {
        AlgorithmResult algorithmResult = algorithmCombinator.getCombinedAlgoResult(recInputParams, activeBundle, recStatusParams.getRecCycleStatus());
        RecResult<FlatRecPayload, FlatRecOtherInfo> recResult = new RecResult<>();
        recStatusParams.setRecResult(recResult);
    }
}
