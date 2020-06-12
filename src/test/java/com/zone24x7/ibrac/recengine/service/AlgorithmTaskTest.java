package com.zone24x7.ibrac.recengine.service;

import com.sun.org.apache.regexp.internal.RE;
import com.zone24x7.ibrac.recengine.pojo.AlgorithmResult;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

/**
 * Test class of AlgorithmTask
 */
class AlgorithmTaskTest {

    private AlgorithmTask algorithmTask;
    private AlgorithmService algorithmService;
    private RecCycleStatus recCycleStatus;

    /**
     * Method to setup the dependencies for the test class
     */
    @BeforeEach
    public void setup() {
        algorithmService = mock(AlgorithmService.class);
        recCycleStatus = mock(RecCycleStatus.class);

        algorithmTask = new AlgorithmTask("100", Collections.emptyMap(), recCycleStatus);
        ReflectionTestUtils.setField(algorithmTask, "algorithmService", algorithmService);
    }

    /**
     * Test to verify algorithm task returns results as expected.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void should_get_algorithm_results_as_expected() throws Exception {
        AlgorithmResult expectedResult = mock(AlgorithmResult.class);
        when(algorithmService.getAlgorithmResult("100", Collections.emptyMap(), recCycleStatus)).thenReturn(expectedResult);

        AlgorithmResult algorithmResult = algorithmTask.call();
        assertThat(algorithmResult, is(equalTo(expectedResult)));
    }
}