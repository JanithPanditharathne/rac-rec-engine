package com.zone24x7.ibrac.recengine.pipeline.flatrecpipeline.handlers;

import com.zone24x7.ibrac.recengine.pojo.RecStatusParams;
import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;

/**
 * Abstract class for handling Recs
 */
public abstract class RecUnitHandler {

    /**
     * Abstract method to be implemented for all RecUnit handler's  sub classes
     *
     * @param recInputParams  the input parameters for recommendation generation
     * @param recStatusParams rec status parameters for recommendation generation
     * @param activeBundle    the activeBundle for recommendation generation
     */
    public abstract void handleTask(RecInputParams recInputParams, RecStatusParams recStatusParams, ActiveBundle activeBundle);
}
