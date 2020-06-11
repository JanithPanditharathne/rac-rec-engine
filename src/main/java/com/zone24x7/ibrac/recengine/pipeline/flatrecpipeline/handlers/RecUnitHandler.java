package com.zone24x7.ibrac.recengine.pipeline.flatrecpipeline.handlers;

import com.zone24x7.ibrac.recengine.pojo.RecStatusParams;
import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;

/**
 * Interface for handling Recs
 */
public interface RecUnitHandler {

    /**
     * Method to be implemented for all RecUnit handler's  sub classes
     *
     * @param recInputParams  the input parameters for recommendation generation
     * @param recStatusParams rec status parameters for recommendation generation
     * @param activeBundle    the activeBundle for recommendation generation
     */
    void handleTask(RecInputParams recInputParams, RecStatusParams recStatusParams, ActiveBundle activeBundle);
}
