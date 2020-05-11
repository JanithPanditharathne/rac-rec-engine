package com.zone24x7.ibrac.recengine.configuration.sync;

import com.zone24x7.ibrac.recengine.configuration.fetch.CsConfigurationTempCache;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    private static final String REC_SLOT_CONFIG = "{\"recSlots\":[]}";
    private static final String REC_CONFIG = "{\"recs\":[]}";
    private static final String BUNDLE_CONFIG = "{\"bundles\":[]}";

    /**
     * Things to run before each test
     */
    @BeforeEach
    void setUp() {
        csConfigurationTempCache = mock(CsConfigurationTempCache.class);

        ReflectionTestUtils.setField(recConfiguration, "csConfigurationTempCache", csConfigurationTempCache);

        ReflectionTestUtils.setField(recConfiguration, "hashOfLastUsedRecSlotConfig", DigestUtils.sha256Hex(REC_SLOT_CONFIG));
        ReflectionTestUtils.setField(recConfiguration, "hashOfLastUsedRecConfig", DigestUtils.sha256Hex(REC_CONFIG));
        ReflectionTestUtils.setField(recConfiguration, "hashOfLastUsedBundleConfig", DigestUtils.sha256Hex(BUNDLE_CONFIG));

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
}