package com.zone24x7.ibrac.recengine.combinationgenerator;

import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test class for AlgoCombinationIteratorProvider
 */
class AlgoCombinationIteratorProviderTest {
    private AlgoCombinationIteratorProvider algoCombinationIteratorProvider;

    @Mock
    private ApplicationContext context;

    @Mock
    private AlgorithmCombinationIterator algorithmCombinationIterator;

    /**
     * Setup method to prepare before running the tests.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        algoCombinationIteratorProvider = new AlgoCombinationIteratorProvider();
        ReflectionTestUtils.setField(algoCombinationIteratorProvider, "context", context);

        when(context.getBean(eq(AlgorithmCombinationIterator.class), anyString(), anyMap(), any(RecCycleStatus.class))).thenReturn(algorithmCombinationIterator);
    }

    /**
     * Test to verify algorithm iterator is returned as expected.
     */
    @Test
    public void should_return_iterator_instance_as_expected() {
        CombinationIterator algorithmCombinationIterator = algoCombinationIteratorProvider.getCombinationIterator("100", Collections.EMPTY_MAP, new RecCycleStatus("requestId"));

        assertEquals(algorithmCombinationIterator, algorithmCombinationIterator);
        verify(algorithmCombinationIterator, times(1)).initialize();
    }
}