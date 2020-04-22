package com.zone24x7.ibrac.recengine.strategy;

import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;

/**
 * Responsible for generating an active bundle object considering input parameters.
 */
public interface ActiveBundleProvider {
    /**
     * Generates an active bundle object execution details according to the given input parameters.
     *
     * @param recInputParams input parameters received.
     * @return an active bundle.
     */
    ActiveBundle getActiveBundle(RecInputParams recInputParams);
}

