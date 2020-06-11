package com.zone24x7.ibrac.recengine.pipeline.flatrecpipeline.handlers;

import com.zone24x7.ibrac.recengine.pojo.*;
import com.zone24x7.ibrac.recengine.service.AlgorithmCombinator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Class to test the CoreRecommendationHandler.
 */
public class CoreRecommendationHandlerTest {
    private CoreRecommendationHandler coreRecommendationHandler;
    private AlgorithmCombinator algorithmCombinator;
    private RecInputParams recInputParams;
    private RecStatusParams recStatusParams;
    private ActiveBundle activeBundle;
    private RecCycleStatus recCycleStatus;
    private MultipleAlgorithmResult algorithmResult;
    private Logger logger;

    /**
     * Setup method.
     */
    @BeforeEach
    public void setup() {
        recInputParams = new RecInputParams();
        recStatusParams = new RecStatusParams();
        activeBundle = mock(ActiveBundle.class);
        recCycleStatus = mock(RecCycleStatus.class);
        algorithmResult = mock(MultipleAlgorithmResult.class);
        algorithmCombinator = mock(AlgorithmCombinator.class);
        logger = mock(Logger.class);

        recStatusParams.setRecCycleStatus(recCycleStatus);
        when(algorithmCombinator.getCombinedAlgoResult(recInputParams, activeBundle, recCycleStatus)).thenReturn(algorithmResult);

        coreRecommendationHandler = new CoreRecommendationHandler();
        ReflectionTestUtils.setField(coreRecommendationHandler, "algorithmCombinator", algorithmCombinator);
    }

    /**
     * Test to verify that the generated algorithm result is set to the status params object correctly.
     */
    @Test
    public void should_set_the_generated_algorithm_result_in_status_params_correctly() {
        coreRecommendationHandler.handleTask(recInputParams, recStatusParams, activeBundle);
        assertThat(recStatusParams.getMultipleAlgorithmResult(), is(equalTo(algorithmResult)));
    }
}
