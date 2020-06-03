package com.zone24x7.ibrac.recengine.strategy;

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
 * Class to test placement task.
 */
public class PlacementTaskTest {
    private PlacementTask placementTask;
    private RecGenerationEngine recGenerationEngine;
    private RecInputParams recInputParams;
    private RecCycleStatus recCycleStatus;
    private RecResult recResult;

    /**
     * Setup method
     */
    @BeforeEach
    public void setup() {
        recInputParams = mock(RecInputParams.class);
        recCycleStatus = mock(RecCycleStatus.class);
        recResult = mock(RecResult.class);
        recGenerationEngine = mock(RecGenerationEngine.class);

        when(recGenerationEngine.getResult(recInputParams, recCycleStatus)).thenReturn(recResult);

        placementTask = new PlacementTask(recInputParams, recCycleStatus);
        ReflectionTestUtils.setField(placementTask, "recGenerationEngine", recGenerationEngine);
    }

    /**
     * Test to verify that rec generation engine is called with correct inputs.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void should_call_rec_generation_engine_with_correct_inputs() throws Exception {
        RecResult actualRecResult = placementTask.call();
        assertThat(actualRecResult, is(equalTo(recResult)));
        verify(recGenerationEngine, times(1)).getResult(recInputParams, recCycleStatus);
    }

}