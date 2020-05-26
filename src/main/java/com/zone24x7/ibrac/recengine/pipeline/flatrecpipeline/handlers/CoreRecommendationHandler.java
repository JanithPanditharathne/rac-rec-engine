package com.zone24x7.ibrac.recengine.pipeline.flatrecpipeline.handlers;

import com.zone24x7.ibrac.recengine.pojo.RecStatusParams;
import com.zone24x7.ibrac.recengine.pojo.*;
import com.zone24x7.ibrac.recengine.service.AlgorithmCombinator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Class to represent the core recommendations handler implementation.
 */
@Component
@Qualifier("coreRecommendationHandler")
public class CoreRecommendationHandler extends RecUnitHandler {

    @Autowired
    private AlgorithmCombinator algorithmCombinator;

    /**
     * Method to handle the task.
     *
     * @param recInputParams  the input parameters for recommendation generation
     * @param recStatusParams rec status parameters for recommendation generation
     * @param activeBundle    the activeBundle for recommendation generation
     */
    @Override
    public void handleTask(RecInputParams recInputParams, RecStatusParams recStatusParams, ActiveBundle activeBundle) {
        MultipleAlgorithmResult multipleAlgorithmResult = algorithmCombinator.getCombinedAlgoResult(recInputParams, activeBundle, recStatusParams.getRecCycleStatus());
        recStatusParams.setMultipleAlgorithmResult(multipleAlgorithmResult);
        RecResult<FlatRecPayload> recResult = new RecResult<>();
        recStatusParams.setRecResult(recResult);
    }
}
