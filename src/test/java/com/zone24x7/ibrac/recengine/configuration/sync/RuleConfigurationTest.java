package com.zone24x7.ibrac.recengine.configuration.sync;

import com.zone24x7.ibrac.recengine.configuration.fetch.CsConfigurationTempCache;
import com.zone24x7.ibrac.recengine.exceptions.MalformedConfigurationException;
import com.zone24x7.ibrac.recengine.pojo.rules.MerchandisingRuleKnowledgeBaseInfo;
import com.zone24x7.ibrac.recengine.rules.merchandisingrules.executors.MerchandisingRuleExecutor;
import com.zone24x7.ibrac.recengine.rules.merchandisingrules.knowledgebase.KnowledgeBaseGenerator;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

/**
 * Test class of RuleConfiguration
 */
class RuleConfigurationTest {
    private RuleConfiguration ruleConfiguration = new RuleConfiguration();
    private CsConfigurationTempCache csConfigurationTempCache;

    private MerchandisingRuleKnowledgeBaseInfo merchandisingRuleKnowledgeBaseInfo;
    private KnowledgeBaseGenerator<String, MerchandisingRuleKnowledgeBaseInfo> knowledgeBaseGenerator;
    private MerchandisingRuleExecutor ruleExecutor;

    private static final String RULE_CONFIG = "{\"rules\":[]}";

    /**
     * Things to run before each test
     */
    @BeforeEach
    void setUp() {
        csConfigurationTempCache = mock(CsConfigurationTempCache.class);
        knowledgeBaseGenerator = mock(KnowledgeBaseGenerator.class);
        merchandisingRuleKnowledgeBaseInfo = mock(MerchandisingRuleKnowledgeBaseInfo.class);
        ruleExecutor = mock(MerchandisingRuleExecutor.class);

        ReflectionTestUtils.setField(ruleConfiguration, "csConfigurationTempCache", csConfigurationTempCache);
        ReflectionTestUtils.setField(ruleConfiguration, "hashOfLastUsedRuleConfig", DigestUtils.sha256Hex(RULE_CONFIG));
        ReflectionTestUtils.setField(ruleConfiguration, "knowledgeBaseGenerator", knowledgeBaseGenerator);
        ReflectionTestUtils.setField(ruleConfiguration, "knowledgeBaseInfo", merchandisingRuleKnowledgeBaseInfo);
        ReflectionTestUtils.setField(ruleConfiguration, "ruleExecutor", ruleExecutor);

        when(csConfigurationTempCache.getConfiguration(CsConfigurationTypes.RULE_CONFIG)).thenReturn(RULE_CONFIG);
    }

    /**
     * Should return success if loading worked well
     */
    @Test
    void should_return_success_if_loading_worked_well() {
        CsConfigurationStatus result = ruleConfiguration.load();
        assertThat(result, equalTo(CsConfigurationStatus.SUCCESS));
        verify(csConfigurationTempCache, times(1)).getConfiguration(CsConfigurationTypes.RULE_CONFIG);

    }

    /**
     * Should return fail if any call returns null
     */
    @Test
    void should_return_failure_if_any_call_returns_null() {
        when(csConfigurationTempCache.getConfiguration(CsConfigurationTypes.RULE_CONFIG)).thenReturn(null);
        CsConfigurationStatus result = ruleConfiguration.load();
        assertThat(result, equalTo(CsConfigurationStatus.FAIL));
    }

    /**
     * Should return fail if any call returns  empty
     */
    @Test
    void should_return_failure_if_any_call_returns_empty() {
        when(csConfigurationTempCache.getConfiguration(CsConfigurationTypes.RULE_CONFIG)).thenReturn("");
        CsConfigurationStatus result = ruleConfiguration.load();
        assertThat(result, equalTo(CsConfigurationStatus.FAIL));
    }

    /**
     * Should return false if at least one has is null
     */
    @Test
    void should_return_false_if_at_least_one_hash_is_null() {
        ReflectionTestUtils.setField(ruleConfiguration, "hashOfLastUsedRuleConfig", null);
        boolean result = ruleConfiguration.configAppliedAtLeastOnce();
        assertThat(result, equalTo(false));
    }

    /**
     * Should return true if at none of the hashes are null
     */
    @Test
    void should_return_true_if_none_of_hashes_are_null() {
        boolean result = ruleConfiguration.configAppliedAtLeastOnce();
        assertThat(result, equalTo(true));
    }

    /**
     * Should return false if old hashes matches new once
     */
    @Test
    void should_return_false_if_old_hashes_matches_new_once() {
        //Hashes are set in setup
        ReflectionTestUtils.setField(ruleConfiguration, "ruleConfig", RULE_CONFIG);
        boolean result = ruleConfiguration.isNewConfiguration();
        assertThat(result, equalTo(false));
    }

    /**
     * Should return false if at least one hash is different
     */
    @Test
    void should_return_false_if_at_least_one_hash_is_different() {
        //Hashes are set in setup
        ReflectionTestUtils.setField(ruleConfiguration, "ruleConfig", "");
        boolean result = ruleConfiguration.isNewConfiguration();
        assertThat(result, equalTo(true));
    }

    /**
     * Should return fail if setting merchandising knowledge base configurations throws an error.
     */
    @Test
    void should_return_fail_if_adding_merchandising_knowledge_base_configuration_throws_an_error() throws MalformedConfigurationException {
        doThrow(MalformedConfigurationException.class).when(knowledgeBaseGenerator).generate(null);

        CsConfigurationStatus result = ruleConfiguration.configure();
        assertThat(result, equalTo(CsConfigurationStatus.FAIL));
    }

    /**
     * Should return success if merchandising knowledge base info is set as expected.
     */
    @Test
    void should_return_success_when_knowledge_base_info_is_set_successfully() throws MalformedConfigurationException {
        when(knowledgeBaseGenerator.generate(anyString())).thenReturn(merchandisingRuleKnowledgeBaseInfo);
        ReflectionTestUtils.setField(ruleConfiguration, "ruleConfig", RULE_CONFIG);
        CsConfigurationStatus result = ruleConfiguration.configure();
        assertThat(result, equalTo(CsConfigurationStatus.SUCCESS));
        verify(knowledgeBaseGenerator, times(1)).generate(anyString());
    }

    /**
     * Should return success if configurations applied successfully.
     */
    @Test
    void should_return_success_if_rule_configurations_applied_successfully() {
        ReflectionTestUtils.setField(ruleConfiguration, "ruleConfig", RULE_CONFIG);
        CsConfigurationStatus result = ruleConfiguration.apply();

        verify(ruleExecutor, times(1)).setKnowledgeBaseInfo(merchandisingRuleKnowledgeBaseInfo);
        assertThat(result, equalTo(CsConfigurationStatus.SUCCESS));
    }
}