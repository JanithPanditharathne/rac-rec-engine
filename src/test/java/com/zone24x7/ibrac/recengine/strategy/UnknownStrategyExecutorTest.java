package com.zone24x7.ibrac.recengine.strategy;

import com.zone24x7.ibrac.recengine.exceptions.SetupException;
import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

/**
 * Class to test UnknownStrategyExecutor.
 */
public class UnknownStrategyExecutorTest {
    private RecInputParams recInputParams;
    private RecCycleStatus recCycleStatus;
    private ActiveBundle activeBundle;

    /**
     * Setup method
     */
    @BeforeEach
    public void setup() {
        recInputParams = mock(RecInputParams.class);
        recCycleStatus = mock(RecCycleStatus.class);
        activeBundle = mock(ActiveBundle.class);
    }

    /**
     * Test to verify that null is returned when executed.
     */
    @Test
    public void should_return_null_when_executed() {
        UnknownStrategyExecutor unknownStrategyExecutor = new UnknownStrategyExecutor();
        assertNull(unknownStrategyExecutor.execute(recInputParams, activeBundle, recCycleStatus));
    }
}