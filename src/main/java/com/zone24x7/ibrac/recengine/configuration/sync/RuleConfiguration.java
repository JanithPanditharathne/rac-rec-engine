package com.zone24x7.ibrac.recengine.configuration.sync;

import com.zone24x7.ibrac.recengine.configuration.fetch.CsConfigurationTempCache;
import com.zone24x7.ibrac.recengine.exceptions.MalformedConfigurationException;
import com.zone24x7.ibrac.recengine.pojo.rules.MerchandisingRuleKnowledgeBaseInfo;
import com.zone24x7.ibrac.recengine.rules.merchandisingrules.executors.MerchandisingRuleExecutor;
import com.zone24x7.ibrac.recengine.rules.merchandisingrules.knowledgebase.KnowledgeBaseGenerator;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Rule configuration class.
 * Sync up required for rule configurations will be managed thorough this class.
 */
@Component
public class RuleConfiguration implements CsConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleConfiguration.class);

    @Autowired
    private CsConfigurationTempCache csConfigurationTempCache;

    @Autowired
    private KnowledgeBaseGenerator<String, MerchandisingRuleKnowledgeBaseInfo> knowledgeBaseGenerator;

    @Autowired
    private MerchandisingRuleExecutor ruleExecutor;

    private String ruleConfig;
    private static String hashOfLastUsedRuleConfig;
    private MerchandisingRuleKnowledgeBaseInfo knowledgeBaseInfo;

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
        // Generate Knowledge base

        CsConfigurationStatus status = CsConfigurationStatus.SUCCESS;

        try {
            knowledgeBaseInfo = knowledgeBaseGenerator.generate(ruleConfig);
        } catch (MalformedConfigurationException e) {
            LOGGER.error("Error reading rule configuration", e);
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
        try {
            // Apply knowledge base
            ruleExecutor.setKnowledgeBaseInfo(knowledgeBaseInfo);
            // Update hash
            updateHashOfLastUsedConfig(ruleConfig);
            return CsConfigurationStatus.SUCCESS;
        } catch (Exception e) {
            LOGGER.error("Error applying rec configuration", e);
            return CsConfigurationStatus.FAIL;
        }
    }

    /**
     * Method to update hash of last used configuration.
     *
     * @param ruleConfig configuration to calculate hash
     */
    private static synchronized void updateHashOfLastUsedConfig(String ruleConfig) {
        hashOfLastUsedRuleConfig = DigestUtils.sha256Hex(ruleConfig);
    }
}
