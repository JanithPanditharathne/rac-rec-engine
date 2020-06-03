package com.zone24x7.ibrac.recengine.strategy;

import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.RecInputParams;
import com.zone24x7.ibrac.recengine.pojo.RecResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlacementTaskFactoryTest {
    private PlacementTaskFactory placementTaskFactory;
    private PlacementTask placementTask;
    private ApplicationContext context;

    private RecInputParams recInputParams;
    private RecCycleStatus recCycleStatus;

    /**
     * Setup method
     */
    @BeforeEach
    public void setup() {
        recInputParams = mock(RecInputParams.class);
        recCycleStatus = mock(RecCycleStatus.class);
        placementTask = mock(PlacementTask.class);
        context = mock(ApplicationContext.class);
        when(context.getBean(PlacementTask.class, recInputParams, recCycleStatus)).thenReturn(placementTask);

        placementTaskFactory = new PlacementTaskFactory();
        ReflectionTestUtils.setField(placementTaskFactory, "context", context);
    }

    /**
     * Test to verify that placement task is created correctly from the task factory.
     */
    @Test
    public void should_create_placement_task_correctly() {
        PlacementTask actualPlacementTask = placementTaskFactory.create(recInputParams, recCycleStatus);
        assertThat(actualPlacementTask, is(equalTo(placementTask)));
        verify(context, times(1)).getBean(PlacementTask.class, recInputParams, recCycleStatus);
    }
}