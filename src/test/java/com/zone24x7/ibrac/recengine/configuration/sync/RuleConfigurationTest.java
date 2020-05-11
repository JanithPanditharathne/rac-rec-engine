package com.zone24x7.ibrac.recengine.configuration.sync;

import com.zone24x7.ibrac.recengine.configuration.fetch.CsConfigurationTempCache;
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
    private static final String RULE_CONFIG = "{\"rules\":[]}";

    /**
     * Things to run before each test
     */
    @BeforeEach
    void setUp() {
        csConfigurationTempCache = mock(CsConfigurationTempCache.class);
        ReflectionTestUtils.setField(ruleConfiguration, "csConfigurationTempCache", csConfigurationTempCache);
        ReflectionTestUtils.setField(ruleConfiguration, "hashOfLastUsedRuleConfig", DigestUtils.sha256Hex(RULE_CONFIG));

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
}