package com.zone24x7.ibrac.recengine.pipeline.flatrecpipeline;


import com.zone24x7.ibrac.recengine.pipeline.RecGenerationStrategy;
import com.zone24x7.ibrac.recengine.pipeline.flatrecpipeline.handlers.RecUnitHandler;
import com.zone24x7.ibrac.recengine.pojo.FlatRecOtherInfo;
import com.zone24x7.ibrac.recengine.pipeline.flatrecpipeline.pojo.RecommendationStatusParams;
import com.zone24x7.ibrac.recengine.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;


/**
 * Only flat algorithms included recommendation generation strategy
 */
@Component
public class FlatRecOnlyIncludedRecGenerationStrategy implements RecGenerationStrategy<InputParams> {

    @Autowired
    @Qualifier("coreRecommendationHandler")
    private RecUnitHandler coreRecommendationHandler;

    /**
     * Generates an experience.
     *
     * @param activeBundle   Active bundle containing the configuration to execute the
     *                       strategy.
     * @param inputParams    input parameter object which includes cid, pgId, placementId, context params, limit
     * @param recCycleStatus the object to track the status of the recommendation generation
     * @return the generated experience.
     */
    @Override
    public RecResult<FlatRecPayload, FlatRecOtherInfo> getRecommendations(ActiveBundle activeBundle, InputParams inputParams, RecCycleStatus recCycleStatus) {
        //Creates a status object. Initialize the with the recommendation limit provided by the channel.
        RecommendationStatusParams recStatusParams = new RecommendationStatusParams();
        recStatusParams.setRecCycleStatus(recCycleStatus);
        recStatusParams.setLimit(5);
        //List of recommendation unit handlers are initialized
        List<RecUnitHandler> handlers = new LinkedList<>();

        //-----------Add Core Recommendations Handlers----------------------//
        //The core recommendations handlers will be added to chain according to the strategy
        addStrategyHandlers(activeBundle, handlers);

        //The unit handlers of the chain will be executed in this loop. If at any stage an EdeExperience object is available in the recStatusParsms
        //that means an result has been generated. So it will be returned
        for (RecUnitHandler handler : handlers) {
            handler.handleTask(inputParams, recStatusParams, activeBundle);
            RecResult<FlatRecPayload, FlatRecOtherInfo> recResult = recStatusParams.getRecResult();
            if (recResult != null) {
                return recResult;
            }
        }
        return null;
    }

    /**
     * Add the needed strategy handlers
     *
     * @param activeBundle activeBundle fo the request
     * @param handlers     all handlers for creating flat recs
     */
    private void addStrategyHandlers(ActiveBundle activeBundle,
                                     List<RecUnitHandler> handlers) {

        //-----------Add Pre Recommendation Generations Handlers------------//
        //Adds strategy initialization handler to the chain
        handlers.add(coreRecommendationHandler);
        //Adds the global manual rules handler to the chain
    }
}
