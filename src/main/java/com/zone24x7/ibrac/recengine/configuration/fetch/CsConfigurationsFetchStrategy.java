package com.zone24x7.ibrac.recengine.configuration.fetch;

public interface CsConfigurationsFetchStrategy {

    /**
     * Method to fetch the configurations via strategy.
     * Fetched configuration will be updated to CSConfigurationTempCache
     *
     * @throws CsConfigurationFetchException if an error occurs when executing strategy
     */
    void fetchConfigurations() throws CsConfigurationFetchException;
}