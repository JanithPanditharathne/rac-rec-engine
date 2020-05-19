package com.zone24x7.ibrac.recengine.configuration.sync;

import com.zone24x7.ibrac.recengine.configuration.fetch.CsConfigurationTempCache;
import com.zone24x7.ibrac.recengine.exceptions.MalformedConfigurationException;
import com.zone24x7.ibrac.recengine.pojo.rules.RecRuleKnowledgeBaseInfo;
import com.zone24x7.ibrac.recengine.rules.recrules.executors.RecRuleExecutor;
import com.zone24x7.ibrac.recengine.rules.recrules.knowledgebase.RecRuleKnowledgeBaseGenerator;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

/**
 * Test class of RecConfiguration
 */
class RecConfigurationTest {

    private RecConfiguration recConfiguration = new RecConfiguration();
    private CsConfigurationTempCache csConfigurationTempCache;
    private RecRuleKnowledgeBaseGenerator recRuleKnowledgeBaseGenerator;
    private Logger logger;

    private static final String REC_SLOT_CONFIG = "{\"recSlots\":[]}";
    private static final String REC_CONFIG = "{\"recs\":[]}";
    private static final String BUNDLE_CONFIG = "{\"bundles\":[]}";
    private RecRuleExecutor recRuleExecutor;

    /**
     * Things to run before each test
     */
    @BeforeEach
    void setUp() {
        csConfigurationTempCache = mock(CsConfigurationTempCache.class);
        recRuleKnowledgeBaseGenerator = mock(RecRuleKnowledgeBaseGenerator.class);
        recRuleExecutor = mock(RecRuleExecutor.class);
        logger = mock(Logger.class);

        ReflectionTestUtils.setField(recConfiguration, "csConfigurationTempCache", csConfigurationTempCache);

        ReflectionTestUtils.setField(recConfiguration, "hashOfLastUsedRecSlotConfig", DigestUtils.sha256Hex(REC_SLOT_CONFIG));
        ReflectionTestUtils.setField(recConfiguration, "hashOfLastUsedRecConfig", DigestUtils.sha256Hex(REC_CONFIG));
        ReflectionTestUtils.setField(recConfiguration, "hashOfLastUsedBundleConfig", DigestUtils.sha256Hex(BUNDLE_CONFIG));
        ReflectionTestUtils.setField(recConfiguration, "recRuleKnowledgeBaseGenerator", recRuleKnowledgeBaseGenerator);
        ReflectionTestUtils.setField(recConfiguration, "logger", logger);
        ReflectionTestUtils.setField(recConfiguration, "recRuleExecutor", recRuleExecutor);

        when(csConfigurationTempCache.getConfiguration(CsConfigurationTypes.REC_SLOT_CONFIG)).thenReturn(REC_SLOT_CONFIG);
        when(csConfigurationTempCache.getConfiguration(CsConfigurationTypes.REC_CONFIG)).thenReturn(REC_CONFIG);
        when(csConfigurationTempCache.getConfiguration(CsConfigurationTypes.BUNDLE_CONFIG)).thenReturn(BUNDLE_CONFIG);
    }

    /**
     * Should return success if loading worked well
     */
    @Test
    void should_return_success_if_loading_worked_well() {
        CsConfigurationStatus result = recConfiguration.load();
        assertThat(result, equalTo(CsConfigurationStatus.SUCCESS));
        verify(csConfigurationTempCache, times(1)).getConfiguration(CsConfigurationTypes.REC_SLOT_CONFIG);
        verify(csConfigurationTempCache, times(1)).getConfiguration(CsConfigurationTypes.REC_CONFIG);
        verify(csConfigurationTempCache, times(1)).getConfiguration(CsConfigurationTypes.BUNDLE_CONFIG);
    }

    /**
     * Should return fail if any call returns null
     */
    @Test
    void should_return_failure_if_any_call_returns_null() {
        when(csConfigurationTempCache.getConfiguration(CsConfigurationTypes.BUNDLE_CONFIG)).thenReturn(null);
        CsConfigurationStatus result = recConfiguration.load();
        assertThat(result, equalTo(CsConfigurationStatus.FAIL));
    }

    /**
     * Should return fail if any call returns  empty
     */
    @Test
    void should_return_failure_if_any_call_returns_empty() {
        when(csConfigurationTempCache.getConfiguration(CsConfigurationTypes.BUNDLE_CONFIG)).thenReturn("");
        CsConfigurationStatus result = recConfiguration.load();
        assertThat(result, equalTo(CsConfigurationStatus.FAIL));
    }

    /**
     * Should return false if at least one has is null
     */
    @Test
    void should_return_false_if_at_least_one_hash_is_null() {
        ReflectionTestUtils.setField(recConfiguration, "hashOfLastUsedBundleConfig", null);
        boolean result = recConfiguration.configAppliedAtLeastOnce();
        assertThat(result, equalTo(false));
    }

    /**
     * Should return true if at none of the hashes are null
     */
    @Test
    void should_return_true_if_none_of_hashes_are_null() {
        boolean result = recConfiguration.configAppliedAtLeastOnce();
        assertThat(result, equalTo(true));
    }

    /**
     * Should return false if old hashes matches new once
     */
    @Test
    void should_return_false_if_old_hashes_matches_new_once() {
        //Hashes are set in setup
        ReflectionTestUtils.setField(recConfiguration, "recSlotConfig", REC_SLOT_CONFIG);
        ReflectionTestUtils.setField(recConfiguration, "recConfig", REC_CONFIG);
        ReflectionTestUtils.setField(recConfiguration, "bundleConfig", BUNDLE_CONFIG);

        boolean result = recConfiguration.isNewConfiguration();

        assertThat(result, equalTo(false));
    }

    /**
     * Should return false if at least one hash is different
     */
    @Test
    void should_return_false_if_at_least_one_hash_is_different() {
        //Hashes are set in setup
        ReflectionTestUtils.setField(recConfiguration, "recSlotConfig", "");
        ReflectionTestUtils.setField(recConfiguration, "recConfig", REC_CONFIG);
        ReflectionTestUtils.setField(recConfiguration, "bundleConfig", BUNDLE_CONFIG);

        boolean result = recConfiguration.isNewConfiguration();

        assertThat(result, equalTo(true));
    }

    /**
     * Should return success status on successful configure
     */
    @Test
    void should_return_success_if_configuration_is_successful() throws MalformedConfigurationException {
        RecRuleKnowledgeBaseInfo recRuleKnowledgeBaseInfo = new RecRuleKnowledgeBaseInfo();
        when(recRuleKnowledgeBaseGenerator.getKnowledgeBaseInfo()).thenReturn(recRuleKnowledgeBaseInfo);

        //Hashes are set in setup
        ReflectionTestUtils.setField(recConfiguration, "recSlotConfig", REC_SLOT_CONFIG);
        ReflectionTestUtils.setField(recConfiguration, "recConfig", REC_CONFIG);
        ReflectionTestUtils.setField(recConfiguration, "bundleConfig", BUNDLE_CONFIG);

        CsConfigurationStatus result = recConfiguration.configure();

        assertThat(result, equalTo(CsConfigurationStatus.SUCCESS));
        verify(recRuleKnowledgeBaseGenerator).getKnowledgeBaseInfo();
    }

    /**
     * Should return fail status on exception received
     */
    @Test
    void should_return_fail_status_on_exception_received() throws MalformedConfigurationException {
        doThrow(new MalformedConfigurationException("")).when(recRuleKnowledgeBaseGenerator).setConfigurations(REC_CONFIG);

        //Hashes are set in setup
        ReflectionTestUtils.setField(recConfiguration, "recSlotConfig", REC_SLOT_CONFIG);
        ReflectionTestUtils.setField(recConfiguration, "recConfig", REC_CONFIG);
        ReflectionTestUtils.setField(recConfiguration, "bundleConfig", BUNDLE_CONFIG);

        CsConfigurationStatus result = recConfiguration.configure();

        assertThat(result, equalTo(CsConfigurationStatus.FAIL));
        verify(recRuleKnowledgeBaseGenerator, never()).getKnowledgeBaseInfo();
    }

    /**
     * Should return success status on successful apply
     */
    @Test
    void should_return_success_if_configuration_apply_is_successful() throws MalformedConfigurationException {
        RecRuleKnowledgeBaseInfo recRuleKnowledgeBaseInfo = new RecRuleKnowledgeBaseInfo();
        ReflectionTestUtils.setField(recConfiguration, "knowledgeBaseInfo", recRuleKnowledgeBaseInfo);

        //Hashes are set in setup
        ReflectionTestUtils.setField(recConfiguration, "recSlotConfig", REC_SLOT_CONFIG);
        ReflectionTestUtils.setField(recConfiguration, "recConfig", REC_CONFIG);
        ReflectionTestUtils.setField(recConfiguration, "bundleConfig", BUNDLE_CONFIG);

        CsConfigurationStatus result = recConfiguration.apply();

        assertThat(result, equalTo(CsConfigurationStatus.SUCCESS));
        verify(recRuleExecutor).setRecRuleKnowledgeBaseInfo(recRuleKnowledgeBaseInfo);
    }


    /**
     * Should return fail status on successful apply
     */
    @Test
    void should_return_fail_if_configuration_apply_is_failed() throws MalformedConfigurationException {
        RecRuleKnowledgeBaseInfo recRuleKnowledgeBaseInfo = new RecRuleKnowledgeBaseInfo();
        ReflectionTestUtils.setField(recConfiguration, "knowledgeBaseInfo", recRuleKnowledgeBaseInfo);


        //Hashes are set in setup
        ReflectionTestUtils.setField(recConfiguration, "recSlotConfig", null);
        ReflectionTestUtils.setField(recConfiguration, "recConfig", REC_CONFIG);
        ReflectionTestUtils.setField(recConfiguration, "bundleConfig", BUNDLE_CONFIG);

        CsConfigurationStatus result = recConfiguration.apply();

        assertThat(result, equalTo(CsConfigurationStatus.FAIL));
        verify(recRuleExecutor, never()).setRecRuleKnowledgeBaseInfo(recRuleKnowledgeBaseInfo);
    }
}