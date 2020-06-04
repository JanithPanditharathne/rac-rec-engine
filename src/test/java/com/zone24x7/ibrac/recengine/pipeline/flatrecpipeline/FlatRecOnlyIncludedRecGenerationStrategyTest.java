package com.zone24x7.ibrac.recengine.pipeline.flatrecpipeline;

import com.zone24x7.ibrac.recengine.pipeline.flatrecpipeline.handlers.AccumulationHandler;
import com.zone24x7.ibrac.recengine.pipeline.flatrecpipeline.handlers.CoreRecommendationHandler;
import com.zone24x7.ibrac.recengine.pojo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

/**
 * Class to test FlatRecOnlyIncludedRecGenerationStrategy.
 */
public class FlatRecOnlyIncludedRecGenerationStrategyTest {
    private FlatRecOnlyIncludedRecGenerationStrategy flatRecOnlyIncludedRecGenerationStrategy;
    private CoreRecommendationHandler coreRecommendationHandler;
    private AccumulationHandler accumulationHandler;
    private RecInputParams recInputParams;
    private RecStatusParams recStatusParams;
    private ActiveBundle activeBundle;
    private RecCycleStatus recCycleStatus;

    /**
     * Setup method.
     */
    @BeforeEach
    public void setup() {
        recInputParams = new RecInputParams();
        recStatusParams = new RecStatusParams();
        activeBundle = mock(ActiveBundle.class);
        recCycleStatus = mock(RecCycleStatus.class);
        recStatusParams.setRecCycleStatus(recCycleStatus);

        coreRecommendationHandler = mock(CoreRecommendationHandler.class);
        accumulationHandler = mock(AccumulationHandler.class);

        flatRecOnlyIncludedRecGenerationStrategy = new FlatRecOnlyIncludedRecGenerationStrategy();
        ReflectionTestUtils.setField(flatRecOnlyIncludedRecGenerationStrategy, "coreRecommendationHandler", coreRecommendationHandler);
        ReflectionTestUtils.setField(flatRecOnlyIncludedRecGenerationStrategy, "accumulationHandler", accumulationHandler);
    }

    /**
     * Test to verify that null is returned when handlers dont set a result.
     */
    @Test
    public void should_return_null_if_handles_dont_set_a_result() {
        RecResult<FlatRecPayload> recommendationResult = flatRecOnlyIncludedRecGenerationStrategy.getRecommendations(activeBundle, recInputParams, recCycleStatus);
        assertNull(recommendationResult);
    }
}