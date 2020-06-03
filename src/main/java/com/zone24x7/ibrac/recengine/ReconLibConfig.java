package com.zone24x7.ibrac.recengine;

import com.zone24x7.ibrac.recengine.util.AppConfigStringConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for injecting Recon Lib related configs.
 */
@Configuration
@ConfigurationProperties(prefix = AppConfigStringConstants.CONFIG_RECONLIB_PREFIX)
public class ReconLibConfig extends HashMap<String, String> {

    /**
     * Method to generate the recon lib configurations map by adding the missing prefixes.
     *
     * @return the recon lib configurations map
     */
    public Map<String, String> getConfigurations() {
        Map<String, String> configurationMapForReconLib = new HashMap<>();
        this.forEach((key, value) -> configurationMapForReconLib.put(AppConfigStringConstants.CONFIG_RECONLIB_PREFIX + "." + key, value));
        return configurationMapForReconLib;
    }
}
