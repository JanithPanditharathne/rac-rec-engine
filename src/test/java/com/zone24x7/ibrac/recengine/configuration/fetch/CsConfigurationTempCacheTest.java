package com.zone24x7.ibrac.recengine.configuration.fetch;

import com.zone24x7.ibrac.recengine.configuration.sync.CsConfigurationTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.*;

/**
 * Test class of CsConfigurationTempCache
 */
class CsConfigurationTempCacheTest {
    private Map<String, String> csConfigurations;
    private ReentrantReadWriteLock reentrantReadWriteLock;
    private CsConfigurationTempCache csConfigurationTempCache = new CsConfigurationTempCache();
    private ReentrantReadWriteLock.ReadLock readLock;
    private ReentrantReadWriteLock.WriteLock writeLock;
    private static final String BUNDLE_CONFIGS = "{\"bundleConfigs\":[]}";
    private static final String REC_SLOT_CONFIGS = "{\"recSlotConfigs\":[]}";
    private static final String REC_CONFIGS = "{\"recConfigs\":[]}";
    private static final String RULE_CONFIGS = "{\"ruleConfigs\":[]}";

    /**
     * Things to run before each test
     */
    @BeforeEach
    void setUp() {
        reentrantReadWriteLock = mock(ReentrantReadWriteLock.class);
        readLock = mock(ReentrantReadWriteLock.ReadLock.class);
        writeLock = mock(ReentrantReadWriteLock.WriteLock.class);

        csConfigurations = spy(new HashMap<>());
        ReflectionTestUtils.setField(csConfigurationTempCache, "reentrantReadWriteLock", reentrantReadWriteLock);
        ReflectionTestUtils.setField(csConfigurationTempCache, "csConfigurations", csConfigurations);

        when(reentrantReadWriteLock.readLock()).thenReturn(readLock);
        when(reentrantReadWriteLock.writeLock()).thenReturn(writeLock);
    }

    /**
     * Should get the available configurations
     */
    @Test
    void should_get_available_configurations() {
        csConfigurations.put(CsConfigurationTypes.BUNDLE_CONFIG.toString(), BUNDLE_CONFIGS);
        String configuration = csConfigurationTempCache.getConfiguration(CsConfigurationTypes.BUNDLE_CONFIG);

        assertThat(configuration, equalTo(BUNDLE_CONFIGS));
        verify(readLock, times(1)).lock();
        verify(readLock, times(1)).unlock();
        verify(writeLock, never()).lock();
        verify(writeLock, never()).unlock();
    }

    /**
     * Should receive null if the available configurations not available
     */
    @Test
    void should_receive_null_if_configurations_not_available() {
        String configuration = csConfigurationTempCache.getConfiguration(CsConfigurationTypes.BUNDLE_CONFIG);

        assertThat(configuration, nullValue());
        verify(readLock, times(1)).lock();
        verify(readLock, times(1)).unlock();
        verify(writeLock, never()).lock();
        verify(writeLock, never()).unlock();
    }

    /**
     * Should set the configurations
     */
    @Test
    void should_set_configurations() {
        csConfigurationTempCache.setConfigurations(REC_SLOT_CONFIGS, REC_CONFIGS, BUNDLE_CONFIGS, RULE_CONFIGS);

        verify(csConfigurations, times(1)).put(CsConfigurationTypes.REC_SLOT_CONFIG.toString(), REC_SLOT_CONFIGS);
        verify(csConfigurations, times(1)).put(CsConfigurationTypes.REC_CONFIG.toString(), REC_CONFIGS);
        verify(csConfigurations, times(1)).put(CsConfigurationTypes.BUNDLE_CONFIG.toString(), BUNDLE_CONFIGS);
        verify(csConfigurations, times(1)).put(CsConfigurationTypes.RULE_CONFIG.toString(), RULE_CONFIGS);

        verify(writeLock, times(1)).lock();
        verify(writeLock, times(1)).unlock();
        verify(readLock, never()).lock();
        verify(readLock, never()).unlock();
    }
}