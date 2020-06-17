package com.zone24x7.ibrac.recengine.configuration.fetch;

import com.zone24x7.ibrac.recengine.configuration.sync.CsConfigurationTypes;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Holds the CS configurations temporarily for decoupling configuration fetching and syncing
 */
@Component
public class CsConfigurationTempCache {
    private Map<String, String> csConfigurations = new LinkedHashMap<>();
    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

    /**
     * Method to get the specific CS configuration for given configuration type
     *
     * @param configurationType the configuration type
     * @return the configuration string
     */
    public String getConfiguration(CsConfigurationTypes configurationType) {
        ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
        try {
            readLock.lock();

            if (csConfigurations.containsKey(configurationType.toString())) {
                return csConfigurations.get(configurationType.toString());
            }
        } finally {
            readLock.unlock();
        }

        return null;
    }

    /**
     * Method to set the CS configurations
     *
     * @param recSlots the recSlot configuration string
     * @param recs     rule configurations string
     * @param bundles  the bundle configuration string
     * @param rules    the rule configuration string
     */
    public void setConfigurations(String recSlots,
                                  String recs,
                                  String bundles,
                                  String rules) {
        ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();
        try {
            writeLock.lock();
            csConfigurations.put(CsConfigurationTypes.REC_SLOT_CONFIG.toString(), recSlots);
            csConfigurations.put(CsConfigurationTypes.REC_CONFIG.toString(), recs);
            csConfigurations.put(CsConfigurationTypes.BUNDLE_CONFIG.toString(), bundles);
            csConfigurations.put(CsConfigurationTypes.RULE_CONFIG.toString(), rules);
        } finally {
            writeLock.unlock();
        }
    }
}
