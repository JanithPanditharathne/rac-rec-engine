package com.zone24x7.ibrac.recengine.configuration.fetch;

import com.zone24x7.ibrac.recengine.util.AppConfigStringConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Implementation of local config fetch strategy.
 */
public class CsConfigurationsFetchLocalConfigStrategy implements CsConfigurationsFetchStrategy {

    @Value(AppConfigStringConstants.CONFIG_SYNC_BACKUP_RECSLOTS_CONFIG)
    private String recSlotsConfig;

    @Value(AppConfigStringConstants.CONFIG_SYNC_BACKUP_RECS_CONFIG)
    private String recsConfig;

    @Value(AppConfigStringConstants.CONFIG_SYNC_BACKUP_BUNDLES_CONFIG)
    private String bundlesConfig;

    @Value(AppConfigStringConstants.CONFIG_SYNC_BACKUP_RULES_CONFIG)
    private String rulesConfig;

    @Autowired
    private CsConfigurationTempCache csConfigurationTempCache;

    /**
     * Method to fetch the configurations via strategy.
     * Fetched configuration will be updated to CSConfigurationTempCache
     *
     * @throws CsConfigurationFetchException if an error occurs when executing strategy
     */
    @Override
    public void fetchConfigurations() throws CsConfigurationFetchException {
        csConfigurationTempCache.setConfigurations(recSlotsConfig, recsConfig, bundlesConfig, rulesConfig);
    }
}
