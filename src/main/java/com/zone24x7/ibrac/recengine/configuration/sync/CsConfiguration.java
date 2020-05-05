package com.zone24x7.ibrac.recengine.configuration.sync;

/**
 * Template for configuration sync up.
 */
public interface CsConfiguration {
    /**
     * Loads a configuration.
     *
     * @return Status of load.
     */
    CsConfigurationStatus load();

    /**
     * Flag indicating that configuration was applied at least once.
     *
     * @return Boolean result.
     */
    boolean configAppliedAtLeastOnce();

    /**
     * Method to find if loaded config is a fresh configuration.
     *
     * @return Boolean result.
     */
    boolean isNewConfiguration();

    /**
     * Uses loaded configuration to prepare configuration.
     *
     * @return Status of preparation.
     */
    CsConfigurationStatus configure();

    /**
     * Applies the configuration to the appropriate component.
     *
     * @return Status of applying the configuration.
     */
    CsConfigurationStatus apply();

}
