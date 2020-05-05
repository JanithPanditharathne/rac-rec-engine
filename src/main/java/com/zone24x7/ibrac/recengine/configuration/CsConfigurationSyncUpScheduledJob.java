package com.zone24x7.ibrac.recengine.configuration;

import com.zone24x7.ibrac.recengine.configuration.fetch.CsConfigurationFetchException;
import com.zone24x7.ibrac.recengine.configuration.fetch.CsConfigurationsFetchStrategy;
import com.zone24x7.ibrac.recengine.configuration.sync.CsConfiguration;
import com.zone24x7.ibrac.recengine.configuration.sync.CsConfigurationStatus;
import com.zone24x7.ibrac.recengine.logging.Log;
import com.zone24x7.ibrac.recengine.util.AppConfigStringConstants;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Task that will be scheduled tp poll for configurations
 */
@Component
public class CsConfigurationSyncUpScheduledJob {

    @Autowired
    private CsConfigurationsFetchStrategy csConfigurationsFetchStrategy;

    @Autowired
    @Qualifier("CsConfigurations")
    private Map<String, CsConfiguration> configurationMap;

    @Autowired
    @Qualifier("ConfigSyncLock")
    private ReentrantReadWriteLock configSyncLock;

    @Log
    private static Logger logger;

    /**
     * Scheduled method to call periodically with the given time interval to initiate configuration sync up
     */
    @Scheduled(fixedRateString = AppConfigStringConstants.CONFIG_SYNC_INTERVAL)
    public void run() {
        logger.info("Started config sync up task");
        if (!initializeCsConfigurationsSyncUp()) {
            logger.error("Error initializing configurations syncup");
            //Return on initialization failure
            return;
        }

        List<CsConfiguration> configurations = new LinkedList(configurationMap.values());

        if (!loadConfigurations(configurations)) {
            // Loading failed
            logger.error("Configurations loading failed");
            if (configurationNotAppliedAtLeastOnce(configurations)) {
                //TODO: Write to call back up
                logger.warn("Valid configuration is not available");
            } else {
                logger.warn("Running with existing config due to configuration loader error");
            }
            return;
        }

        if (!generateConfigurations(configurations)) {
            logger.error("Error generating configurations");
            return;
        }

        if (applyConfigurations(configurations)) {
            logger.info("Finished configurations apply");
        } else {
            logger.error("Error applying configurations");
        }
    }

    /**
     * Method to initialize the configuration sync strategy
     *
     * @return true if sync up success
     */
    private boolean initializeCsConfigurationsSyncUp() {
        try {
            csConfigurationsFetchStrategy.fetchConfigurations();
            return true;
        } catch (CsConfigurationFetchException e) {
            logger.error("Error while fetching CS configurations", e);
            return false;
        }
    }

    /**
     * Loads configuration for consideration
     *
     * @param configurations configurations to load
     * @return true if loading success
     */
    private boolean loadConfigurations(List<CsConfiguration> configurations) {
        for (CsConfiguration configuration : configurations) {
            CsConfigurationStatus status = configuration.load();
            if (status != CsConfigurationStatus.SUCCESS) {
                return false;
            }
        }
        return true;
    }

    /**
     * Generate configurations in code applicable format. Configs are converted from code applicable format from raw string
     *
     * @param csConfigurations List of configurations
     */
    private boolean generateConfigurations(List<CsConfiguration> csConfigurations) {
        // Parse and try to generate new configurations
        for (CsConfiguration configuration : csConfigurations) {
            CsConfigurationStatus status = configuration.configure();
            if (status != CsConfigurationStatus.SUCCESS) {
                return false;
            }
        }
        return true;
    }

    /**
     * Apply new configurations
     *
     * @param csConfigurations Cs configs
     * @return true if applying configurations is successful
     */
    private boolean applyConfigurations(List<CsConfiguration> csConfigurations) {
        try {
            configSyncLock.writeLock().lock();
            boolean result = applyConfiguration(csConfigurations);
            if (!result) {
                logger.error("Error applying CS dependent configuration! RE might be in an undefined state!");
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error("Exception in applying configurations", e);
            return false;
        } finally {
            configSyncLock.writeLock().unlock();
        }
    }

    /**
     * Checks whether at least once a configuration has been applied
     *
     * @param configurations configurations to check
     * @return true if at least one configuration not applied at least once
     */
    private boolean configurationNotAppliedAtLeastOnce(List<CsConfiguration> configurations) {
        for (CsConfiguration configuration : configurations) {
            if (!configuration.configAppliedAtLeastOnce()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Applies a configuration list
     *
     * @param configurations List of configurations
     * @return Status of applying
     */
    private boolean applyConfiguration(List<CsConfiguration> configurations) {
        for (CsConfiguration configuration : configurations) {
            if (!configuration.isNewConfiguration()) {
                continue;
            }
            CsConfigurationStatus status = configuration.apply();
            if (status != CsConfigurationStatus.SUCCESS) {
                return false;
            }
        }
        return true;
    }
}