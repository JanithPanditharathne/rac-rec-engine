package com.zone24x7.ibrac.recengine.pipeline.flatrecpipeline.handlers;

import com.zone24x7.ibrac.recengine.pipeline.flatrecpipeline.pojo.RecommendationStatusParams;
import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.InputParams;

/**
 * Abstract class for handling Recs
 */
public abstract class RecUnitHandler {

    /**
     * Abstract method to be implemented for all Recunit handler's  sub classes
     *
     * @param recInputParams  rec input params
     * @param recStatusParams rec status params
     * @param activeBundle    activeBundle
     */
    public abstract void handleTask(InputParams recInputParams, RecommendationStatusParams recStatusParams, ActiveBundle activeBundle);
}
