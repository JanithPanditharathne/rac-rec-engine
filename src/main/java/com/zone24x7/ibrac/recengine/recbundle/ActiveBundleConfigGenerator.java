package com.zone24x7.ibrac.recengine.recbundle;

import com.zone24x7.ibrac.recengine.exceptions.MalformedConfigurationException;
import com.zone24x7.ibrac.recengine.pojo.recbundle.ActiveBundleProviderConfig;

/**
 * Interface for active bundle config generator.
 */
public interface ActiveBundleConfigGenerator {

    /**
     * Method to generate the active bundle provider configuration.
     *
     * @param recSlotConfiguration the rec slots configuration string
     * @param bundleConfiguration  the bundle configuration string
     * @return the active bundle provider configuration
     * @throws MalformedConfigurationException if the configuration are malformed
     */
    ActiveBundleProviderConfig generateConfiguration(String recSlotConfiguration, String bundleConfiguration) throws MalformedConfigurationException;
}
