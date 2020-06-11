package com.zone24x7.ibrac.recengine.service;

import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class of AlgorithmTaskFactory
 */
class AlgorithmTaskFactoryTest {
    private AlgorithmTaskFactory algorithmTaskFactory;

    @Mock
    private ApplicationContext context;

    @Mock
    private RecCycleStatus recCycleStatus;

    /**
     * Setup method to prepare before running the tests.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        algorithmTaskFactory = new AlgorithmTaskFactory();

        ReflectionTestUtils.setField(algorithmTaskFactory, "context", context);
        when(context.getBean(eq(AlgorithmTask.class), anyString(), anyMap(), any(RecCycleStatus.class))).thenReturn(mock(AlgorithmTask.class));
    }

    /**
     * Test to verify algorithm task is created and returned as expected.
     */
    @Test
    public void should_create_and_return_algo_task_instance_as_expected() {
        AlgorithmTask algorithmTaskResult = algorithmTaskFactory.create("100", Collections.emptyMap(), recCycleStatus);
        assertNotNull(algorithmTaskResult);
    }
}