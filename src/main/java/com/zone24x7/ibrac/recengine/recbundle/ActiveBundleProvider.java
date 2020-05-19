package com.zone24x7.ibrac.recengine.recbundle;

import com.zone24x7.ibrac.recengine.exceptions.SetupException;
import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;
import com.zone24x7.ibrac.recengine.pojo.recbundle.ActiveBundleProviderConfig;

import java.util.Optional;

/**
 * Interface for active bundle provider which generates and active bundle based on the input parameters.
 */
public interface ActiveBundleProvider {
    /**
     * Method to generate an active bundle object execution details according to the given input parameters.
     *
     * @param recInputParams input parameters received
     * @param recCycleStatus the recommendation generation tracking object
     * @return an active bundle
     * @throws SetupException if rec slots and bundles are not setup
     */
    Optional<ActiveBundle> getActiveBundle(RecInputParams recInputParams, RecCycleStatus recCycleStatus) throws SetupException;

    /**
     * Method to set the active bundle provider configurations.
     *
     * @param config the active bundle provider configurations
     */
    void setConfig(ActiveBundleProviderConfig config);
}

