package com.zone24x7.ibrac.recengine.configuration.sync;

import com.zone24x7.ibrac.recengine.configuration.fetch.CsConfigurationTempCache;
import com.zone24x7.ibrac.recengine.exceptions.MalformedConfigurationException;
import com.zone24x7.ibrac.recengine.logging.Log;
import com.zone24x7.ibrac.recengine.pojo.recbundle.ActiveBundleProviderConfig;
import com.zone24x7.ibrac.recengine.pojo.rules.RecRuleKnowledgeBaseInfo;
import com.zone24x7.ibrac.recengine.recbundle.ActiveBundleConfigGenerator;
import com.zone24x7.ibrac.recengine.recbundle.ActiveBundleProvider;
import com.zone24x7.ibrac.recengine.rules.merchandisingrules.knowledgebase.KnowledgeBaseGenerator;
import com.zone24x7.ibrac.recengine.rules.recrules.executors.RecRuleExecutor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Recommendation configuration class.
 * Sync up required for recommendation configurations will be managed thorough this class.
 */
@Component
public class RecConfiguration implements CsConfiguration {
    @Autowired
    private CsConfigurationTempCache csConfigurationTempCache;

    @Autowired
    private KnowledgeBaseGenerator<String, RecRuleKnowledgeBaseInfo> recRuleKnowledgeBaseGenerator;

    @Autowired
    private RecRuleExecutor recRuleExecutor;

    @Autowired
    private ActiveBundleProvider activeBundleProvider;

    @Autowired
    private ActiveBundleConfigGenerator activeBundleConfigGenerator;

    @Log
    private Logger logger;

    private String recSlotConfig;
    private String recConfig;
    private String bundleConfig;
    private static String hashOfLastUsedRecSlotConfig;
    private static String hashOfLastUsedRecConfig;
    private static String hashOfLastUsedBundleConfig;
    private RecRuleKnowledgeBaseInfo knowledgeBaseInfo;
    private ActiveBundleProviderConfig activeBundleProviderConfig;

    /**
     * Loads latest configuration from the cache.
     *
     * @return Config status on success or failure.
     */
    @Override
    public CsConfigurationStatus load() {
        String tempRecSlotConfig = csConfigurationTempCache.getConfiguration(CsConfigurationTypes.REC_SLOT_CONFIG);
        String tempRecConfig = csConfigurationTempCache.getConfiguration(CsConfigurationTypes.REC_CONFIG);
        String tempBundleConfig = csConfigurationTempCache.getConfiguration(CsConfigurationTypes.BUNDLE_CONFIG);

        if (StringUtils.isAnyEmpty(tempRecSlotConfig, tempRecConfig, tempBundleConfig)) {
            return CsConfigurationStatus.FAIL;
        } else {
            this.recSlotConfig = tempRecSlotConfig;
            this.recConfig = tempRecConfig;
            this.bundleConfig = tempBundleConfig;
            return CsConfigurationStatus.SUCCESS;
        }
    }

    /**
     * Check whether configuration has been applied at least once.
     *
     * @return true if applied at least once.
     */
    @Override
    public boolean configAppliedAtLeastOnce() {
        return StringUtils.isNoneEmpty(hashOfLastUsedRecSlotConfig, hashOfLastUsedRecConfig, hashOfLastUsedBundleConfig);
    }

    /**
     * Checks whether the its a new configuration.
     *
     * @return true if its a new configuration.
     */
    @Override
    public boolean isNewConfiguration() {
        String recSlotConfigHash = DigestUtils.sha256Hex(recSlotConfig);
        String recConfigHash = DigestUtils.sha256Hex(recConfig);
        String bundleConfigHash = DigestUtils.sha256Hex(bundleConfig);

        boolean shouldUpdateRecSlotRuleConfigs = !StringUtils.equals(recSlotConfigHash, hashOfLastUsedRecSlotConfig);
        boolean shouldUpdateRecConfigs = !StringUtils.equals(recConfigHash, hashOfLastUsedRecConfig);
        boolean shouldUpdateBundleConfigs = !StringUtils.equals(bundleConfigHash, hashOfLastUsedBundleConfig);

        return shouldUpdateRecSlotRuleConfigs || shouldUpdateRecConfigs || shouldUpdateBundleConfigs;
    }

    /**
     * Configures the configuration into required format.
     *
     * @return Config status on success or failure.
     */
    @Override
    public CsConfigurationStatus configure() {
        //Generate knowledgeBaseInfo
        CsConfigurationStatus status = CsConfigurationStatus.SUCCESS;

        try {
            activeBundleProviderConfig = activeBundleConfigGenerator.generateConfiguration(recSlotConfig, bundleConfig);
            recRuleKnowledgeBaseGenerator.setConfigurations(recConfig);
            knowledgeBaseInfo = recRuleKnowledgeBaseGenerator.getKnowledgeBaseInfo();
        } catch (MalformedConfigurationException e) {
            logger.error("Error in configuring rec configuration", e);
            status = CsConfigurationStatus.FAIL;
        }

        return status;
    }

    /**
     * Applies the configuration to the appropriate component.
     *
     * @return Status of applying the configuration.
     */
    @Override
    public CsConfigurationStatus apply() {
        //Apply knowledgeBaseInfo
        CsConfigurationStatus status = CsConfigurationStatus.SUCCESS;

        try {
            activeBundleProvider.setConfig(activeBundleProviderConfig);
            recRuleExecutor.setRecRuleKnowledgeBaseInfo(knowledgeBaseInfo);
            updateHashOfLastUsedConfig(recSlotConfig, recConfig, bundleConfig);
        } catch (Exception e) {
            logger.error("Error applying rec configuration", e);
            status = CsConfigurationStatus.FAIL;
        }

        return status;
    }

    /**
     * Method to update the hash of the last used configuration.
     *
     * @param recSlotConfig recSlotConfig configuration.
     * @param recConfig     recConfig configuration.
     * @param bundleConfig  bundleConfig   configuration.
     */
    private static synchronized void updateHashOfLastUsedConfig(String recSlotConfig, String recConfig, String bundleConfig) {
        hashOfLastUsedRecSlotConfig = DigestUtils.sha256Hex(recSlotConfig);
        hashOfLastUsedRecConfig = DigestUtils.sha256Hex(recConfig);
        hashOfLastUsedBundleConfig = DigestUtils.sha256Hex(bundleConfig);
    }
}