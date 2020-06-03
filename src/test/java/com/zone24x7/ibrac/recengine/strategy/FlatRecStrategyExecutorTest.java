package com.zone24x7.ibrac.recengine.strategy;

import com.zone24x7.ibrac.recengine.pipeline.flatrecpipeline.FlatRecOnlyIncludedRecGenerationStrategy;
import com.zone24x7.ibrac.recengine.pojo.ActiveBundle;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;
import com.zone24x7.ibrac.recengine.pojo.RecResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

/**
 * Class to test FlatRecStrategyExecutor.
 */
public class FlatRecStrategyExecutorTest {
    private FlatRecOnlyIncludedRecGenerationStrategy flatRecOnlyIncludedRecGenerationStrategy;
    private FlatRecStrategyExecutor flatRecStrategyExecutor;
    private RecInputParams recInputParams;
    private ActiveBundle activeBundle;
    private RecCycleStatus recCycleStatus;
    private RecResult recResult;
    private StrategyExecutor nextExecutor;

    /**
     * Setup method.
     */
    @BeforeEach
    public void setup() {
        flatRecOnlyIncludedRecGenerationStrategy = mock(FlatRecOnlyIncludedRecGenerationStrategy.class);
        recInputParams = mock(RecInputParams.class);
        activeBundle = mock(ActiveBundle.class);
        recCycleStatus = mock(RecCycleStatus.class);
        recResult = mock(RecResult.class);
        nextExecutor = mock(StrategyExecutor.class);

        when(flatRecOnlyIncludedRecGenerationStrategy.getRecommendations(activeBundle, recInputParams, recCycleStatus)).thenReturn(recResult);

        flatRecStrategyExecutor = new FlatRecStrategyExecutor();
        flatRecStrategyExecutor.setNextExecutor(nextExecutor);

        ReflectionTestUtils.setField(flatRecStrategyExecutor, "flatRecOnlyIncludedRecGenerationStrategy", flatRecOnlyIncludedRecGenerationStrategy);
    }

    /**
     * Test to verify that flat rec only strategy is called when the active bundle type is flat.
     */
    @Test
    public void should_call_flat_rec_only_strategy_if_active_bundle_type_is_flat() {
        when(activeBundle.getType()).thenReturn("flat");
        RecResult actualRecResult = flatRecStrategyExecutor.execute(recInputParams, activeBundle, recCycleStatus);
        assertThat(actualRecResult, is(equalTo(recResult)));
    }

    /**
     * Test to verify that next strategy is called when the active bundle type is not flat.
     */
    @Test
    public void should_call_next_executor_when_active_bundle_type_is_not_flat() {
        when(activeBundle.getType()).thenReturn("some other type");
        flatRecStrategyExecutor.execute(recInputParams, activeBundle, recCycleStatus);
        verify(nextExecutor, times(1)).execute(recInputParams, activeBundle, recCycleStatus);
    }
}