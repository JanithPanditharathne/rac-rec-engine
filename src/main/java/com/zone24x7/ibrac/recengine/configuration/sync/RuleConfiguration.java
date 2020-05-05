package com.zone24x7.ibrac.recengine.configuration.sync;

import com.zone24x7.ibrac.recengine.configuration.fetch.CsConfigurationTempCache;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Rule configuration class.
 * Sync up required for rule configurations will be managed thorough this class.
 */
@Component
public class RuleConfiguration implements CsConfiguration {
    @Autowired
    private CsConfigurationTempCache csConfigurationTempCache;

    private String ruleConfig;
    private static String hashOfLastUsedRuleConfig;

    /**
     * Loads latest configuration from the config manager.
     *
     * @return Config status on success or failure.
     */
    @Override
    public CsConfigurationStatus load() {
        String tempRuleConfig = csConfigurationTempCache.getConfiguration(CsConfigurationTypes.RULE_CONFIG);

        if (StringUtils.isEmpty(tempRuleConfig)) {
            return CsConfigurationStatus.FAIL;
        } else {
            this.ruleConfig = tempRuleConfig;
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
        return StringUtils.isNotEmpty(hashOfLastUsedRuleConfig);
    }

    /**
     * Checks whether the its a new configuration.
     *
     * @return true if its a new configuration.
     */
    @Override
    public boolean isNewConfiguration() {
        String ruleConfigHash = DigestUtils.sha256Hex(ruleConfig);
        return !StringUtils.equals(ruleConfigHash, hashOfLastUsedRuleConfig);
    }

    /**
     * Configures the configuration into required format.
     *
     * @return Config status on success or failure.
     */
    @Override
    public CsConfigurationStatus configure() {
        //Generate Knowledge base

        /*
         * ConfigStatus status = ConfigStatus.SUCCESS;
         *         try {
         *             activeBundleProviderConfig = activeBundleProviderConfigGenerator.generateConfiguration(placementConfig, bundleConfig, channelPlacementRuleConfig);
         *             channelPlacementRuleKnowledgeBaseGenerator.setRuleConfigurations(activeBundleProviderConfig.getChannelPlacementRules());
         *             channelPlacementRuleKnowledgeBase = channelPlacementRuleKnowledgeBaseGenerator.getRuleKnowledgeBase();
         *             placementValidator = channelPlacementRuleKnowledgeBaseGenerator.getPlacementValidator();
         *         } catch (MalformedConfigurationException e) {
         *             LOGGER.error("Error in trying placement configuration", e);
         *             status = ConfigStatus.FAIL;
         *             configurationStatus.setLastSyncedState("FAIL : " + LogUtilities.getMinifiedMessage(e));
         *         }
         *
         *         return status;
         */
        return CsConfigurationStatus.SUCCESS;
    }

    /**
     * Applies the configuration to the appropriate component.
     *
     * @return Status of applying the configuration.
     */
    @Override
    public CsConfigurationStatus apply() {
        //Apply knowledge base

        /*
         * ConfigStatus status = ConfigStatus.SUCCESS;
         *         try {
         *             activeBundleProvider.updateConfiguration(activeBundleProviderConfig);
         *             channelPlacementRuleExecutor.setKnowledgeBase(channelPlacementRuleKnowledgeBase);
         *             channelPlacementRuleExecutor.setPlacementValidator(placementValidator);
         *             updateHashOfLastUsedConfig(placementConfig, bundleConfig, channelPlacementRuleConfig);
         *             configurationStatus.setLastSyncedPlacementConfigs(placementConfig);
         *             configurationStatus.setLastSyncedBundleConfigs(bundleConfig);
         *             configurationStatus.setLastSyncedChannelPlacementRuleConfigs(channelPlacementRuleConfig);
         *         } catch (Exception e) {
         *             LOGGER.error("Error applying placement configuration", e);
         *             status = ConfigStatus.FAIL;
         *         }
         *         return status;
         */

        // Update hash
        hashOfLastUsedRuleConfig = DigestUtils.sha256Hex(ruleConfig);
        return CsConfigurationStatus.SUCCESS;
    }


}
