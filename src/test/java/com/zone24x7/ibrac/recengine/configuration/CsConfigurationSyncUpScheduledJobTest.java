package com.zone24x7.ibrac.recengine.configuration;

import com.zone24x7.ibrac.recengine.configuration.fetch.CsConfigurationFetchException;
import com.zone24x7.ibrac.recengine.configuration.fetch.CsConfigurationsFetchStrategy;
import com.zone24x7.ibrac.recengine.configuration.sync.CsConfiguration;
import com.zone24x7.ibrac.recengine.configuration.sync.CsConfigurationStatus;
import com.zone24x7.ibrac.recengine.configuration.sync.RecConfiguration;
import com.zone24x7.ibrac.recengine.configuration.sync.RuleConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.mockito.Mockito.*;

/**
 * Test class for CsConfigurationSyncUpScheduledJob class
 */
class CsConfigurationSyncUpScheduledJobTest {

    private CsConfigurationSyncUpScheduledJob csJob = new CsConfigurationSyncUpScheduledJob();
    private CsConfigurationsFetchStrategy csConfigurationsFetchStrategy;
    private Logger logger;
    private Map<String, CsConfiguration> configurationMap;
    private ReentrantReadWriteLock configSyncLock;
    private ReentrantReadWriteLock.WriteLock writeLock;
    private ReentrantReadWriteLock.ReadLock readLock;
    private RecConfiguration recConfigurationMock;
    private RuleConfiguration ruleConfigurationMock;

    /**
     * Setup mock classes
     */
    @BeforeEach
    void setUp() {
        csConfigurationsFetchStrategy = mock(CsConfigurationsFetchStrategy.class);
        logger = mock(Logger.class);
        Map<String, CsConfiguration> configMap = new HashMap<>();
        configurationMap = spy(configMap);
        configSyncLock = mock(ReentrantReadWriteLock.class);
        writeLock = mock(ReentrantReadWriteLock.WriteLock.class);
        readLock = mock(ReentrantReadWriteLock.ReadLock.class);

        recConfigurationMock = mock(RecConfiguration.class);
        ruleConfigurationMock = mock(RuleConfiguration.class);
        configurationMap.put("rec", recConfigurationMock);
        configurationMap.put("rule", ruleConfigurationMock);

        ReflectionTestUtils.setField(csJob, "csConfigurationsFetchStrategy", csConfigurationsFetchStrategy);
        ReflectionTestUtils.setField(csJob, "logger", logger);
        ReflectionTestUtils.setField(csJob, "configurationMap", configurationMap);
        ReflectionTestUtils.setField(csJob, "configSyncLock", configSyncLock);

        when(configSyncLock.writeLock()).thenReturn(writeLock);
        when(configSyncLock.readLock()).thenReturn(readLock);

        when(recConfigurationMock.load()).thenReturn(CsConfigurationStatus.SUCCESS);
        when(ruleConfigurationMock.load()).thenReturn(CsConfigurationStatus.SUCCESS);

        when(recConfigurationMock.isNewConfiguration()).thenReturn(true);
        when(ruleConfigurationMock.isNewConfiguration()).thenReturn(true);

        when(recConfigurationMock.configAppliedAtLeastOnce()).thenReturn(true);
        when(ruleConfigurationMock.configAppliedAtLeastOnce()).thenReturn(true);

        when(recConfigurationMock.configure()).thenReturn(CsConfigurationStatus.SUCCESS);
        when(ruleConfigurationMock.configure()).thenReturn(CsConfigurationStatus.SUCCESS);

        when(recConfigurationMock.apply()).thenReturn(CsConfigurationStatus.SUCCESS);
        when(ruleConfigurationMock.apply()).thenReturn(CsConfigurationStatus.SUCCESS);
    }

    /**
     * Should return after logging an error
     *
     * @throws CsConfigurationFetchException exception at failure
     */
    @Test
    void should_log_and_return_if_exception_received_from_strategy() throws CsConfigurationFetchException {
        doThrow(new CsConfigurationFetchException()).when(csConfigurationsFetchStrategy).fetchConfigurations();
        csJob.run();
        verify(logger, times(1)).error("Error initializing configurations syncup");
    }

    /**
     * Should call all the methods in config obejcts all went well
     */
    @Test
    void should_call_methods_in_all_config_mocks_when_working_in_happy_path() {
        csJob.run();

        verify(configurationMap, times(1)).values();

        verify(recConfigurationMock, times(1)).load();
        verify(ruleConfigurationMock, times(1)).load();

        verify(recConfigurationMock, times(1)).isNewConfiguration();
        verify(ruleConfigurationMock, times(1)).isNewConfiguration();

        verify(recConfigurationMock, times(1)).configure();
        verify(ruleConfigurationMock, times(1)).configure();

        verify(recConfigurationMock, times(1)).apply();
        verify(ruleConfigurationMock, times(1)).apply();

        verify(logger, times(1)).info("Finished configurations apply");
    }

    /**
     * Should return from load if one load is failed
     */
    @Test
    void should_call_return_from_load_if_one_configuration_load_is_failed() {
        //Marking failure for load ruleConfig
        when(ruleConfigurationMock.load()).thenReturn(CsConfigurationStatus.FAIL);

        csJob.run();

        verify(configurationMap, times(1)).values();

        verify(recConfigurationMock, times(1)).load();
        verify(ruleConfigurationMock, times(1)).load();

        verify(recConfigurationMock, never()).isNewConfiguration();
        verify(ruleConfigurationMock, never()).isNewConfiguration();

        verify(recConfigurationMock, never()).configure();
        verify(ruleConfigurationMock, never()).configure();

        verify(recConfigurationMock, never()).apply();
        verify(ruleConfigurationMock, never()).apply();

        verify(logger, times(1)).error("Configurations loading failed");
    }

    /**
     * Should return from configure if one configure is failed
     */
    @Test
    void should_call_return_from_configure_if_one_configuration_configure_is_failed() {
        //Marking configure failure for ruleConfig
        when(ruleConfigurationMock.configure()).thenReturn(CsConfigurationStatus.FAIL);

        csJob.run();

        verify(configurationMap, times(1)).values();

        verify(recConfigurationMock, times(1)).load();
        verify(ruleConfigurationMock, times(1)).load();

        verify(recConfigurationMock, never()).isNewConfiguration();
        verify(ruleConfigurationMock, never()).isNewConfiguration();

        verify(recConfigurationMock, times(1)).configure();
        verify(ruleConfigurationMock, times(1)).configure();

        verify(recConfigurationMock, never()).apply();
        verify(ruleConfigurationMock, never()).apply();

        verify(logger, times(1)).error("Error generating configurations");
    }

    /**
     * Should return from apply if one apply is failed
     */
    @Test
    void should_return_from_apply_if_one_configuration_apply_is_failed() {
        //Marking configure failure for ruleConfig
        when(ruleConfigurationMock.apply()).thenReturn(CsConfigurationStatus.FAIL);

        csJob.run();

        verify(configurationMap, times(1)).values();

        verify(recConfigurationMock, times(1)).load();
        verify(ruleConfigurationMock, times(1)).load();

        verify(recConfigurationMock, times(1)).configure();
        verify(ruleConfigurationMock, times(1)).configure();

        verify(recConfigurationMock, times(1)).apply();
        verify(ruleConfigurationMock, times(1)).apply();

        verify(recConfigurationMock, times(1)).isNewConfiguration();
        verify(ruleConfigurationMock, times(1)).isNewConfiguration();

        verify(logger, times(1)).error("Error applying configurations");
    }


    /**
     * Should not call apply if it is not a new configuration
     */
    @Test
    void should_not_call_apply_if_it_is_not_a_new_configuration() {
        //Marking return false for rule configuration
        when(ruleConfigurationMock.isNewConfiguration()).thenReturn(false);

        csJob.run();

        verify(recConfigurationMock, times(1)).apply();
        verify(ruleConfigurationMock, never()).apply();
    }

    /**
     * Should not log and return if load failed and configuration is not applied at least once
     */
    @Test
    void should_log_and_return_if_load_failed_and_config_has_not_got_applied_at_least_once() {
        //Marking failure for load ruleConfig
        when(ruleConfigurationMock.load()).thenReturn(CsConfigurationStatus.FAIL);
        //Rule config not applied at least once
        when(ruleConfigurationMock.configAppliedAtLeastOnce()).thenReturn(false);

        csJob.run();

        verify(recConfigurationMock, never()).apply();
        verify(ruleConfigurationMock, never()).apply();
        verify(logger, times(1)).warn("Valid configuration is not available");
    }

    /**
     * Should log and return if apply failed due to exception
     */
    @Test
    void should_log_and_return_if_apply_failed_due_to_exception() {
        doThrow(new NullPointerException()).when(recConfigurationMock).apply();
        csJob.run();
        verify(logger).error("Error applying configurations");
    }
}