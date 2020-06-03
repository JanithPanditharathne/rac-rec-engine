package com.zone24x7.ibrac.recengine.strategy;

import com.zone24x7.ibrac.recengine.exceptions.SetupException;
import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;
import com.zone24x7.ibrac.recengine.pojo.RecResult;
import com.zone24x7.ibrac.recengine.recbundle.ActiveBundleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Class to test RecGenerationStrategyExecutorEngine.
 */
public class RecGenerationStrategyExecutorEngineTest {
    private RecGenerationStrategyExecutorEngine engine;
    private StrategyExecutor strategyExecutor;
    private ActiveBundleProvider activeBundleProvider;
    private Logger logger;
    private RecInputParams recInputParams;
    private RecInputParams invalidRecInputParams;
    private RecCycleStatus recCycleStatus;
    private RecResult recResult;
    private ActiveBundle activeBundle;

    /**
     * Setup method
     */
    @BeforeEach
    public void setup() throws SetupException {
        recInputParams = mock(RecInputParams.class);
        invalidRecInputParams = mock(RecInputParams.class);
        recCycleStatus = mock(RecCycleStatus.class);
        recResult = mock(RecResult.class);
        activeBundle = mock(ActiveBundle.class);

        strategyExecutor = mock(StrategyExecutor.class);
        activeBundleProvider = mock(ActiveBundleProvider.class);
        logger = mock(Logger.class);

        when(activeBundleProvider.getActiveBundle(recInputParams, recCycleStatus)).thenReturn(Optional.of(activeBundle));
        when(activeBundleProvider.getActiveBundle(invalidRecInputParams, recCycleStatus)).thenReturn(Optional.empty());

        when(strategyExecutor.execute(recInputParams, activeBundle, recCycleStatus)).thenReturn(recResult);

        engine = new RecGenerationStrategyExecutorEngine();
        ReflectionTestUtils.setField(engine, "strategyExecutor", strategyExecutor);
        ReflectionTestUtils.setField(engine, "activeBundleProvider", activeBundleProvider);
        ReflectionTestUtils.setField(engine, "logger", logger);
    }

    /**
     * Test to verify that a correct rec result is generate when the active bundle is found.
     */
    @Test
    public void should_generate_the_result_correctly_when_active_bundle_is_found() {
        RecResult<?> actualResult = engine.getResult(recInputParams, recCycleStatus);
        assertThat(actualResult, is(equalTo(recResult)));
    }

    /**
     * Test to verify that a null result is returned when the active bundle is not found.
     */
    @Test
    public void should_return_null_when_active_bundle_is_not_found() {
        RecResult<?> actualResult = engine.getResult(invalidRecInputParams, recCycleStatus);
        assertNull(actualResult);
    }

    /**
     * Test to verify that a null result is returned if active bundle provider throws an exception.
     */
    @Test
    public void should_return_null_if_setup_exception_is_thrown_from_active_bundle_provider() throws SetupException {
        when(activeBundleProvider.getActiveBundle(recInputParams, recCycleStatus)).thenThrow(SetupException.class);
        RecResult<?> actualResult = engine.getResult(recInputParams, recCycleStatus);
        assertNull(actualResult);
    }
}