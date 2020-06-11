package com.zone24x7.ibrac.recengine.service;

import com.zone24x7.ibrac.recengine.dao.DatasourceAdapter;
import com.zone24x7.ibrac.recengine.exceptions.BaseConnectionException;
import com.zone24x7.ibrac.recengine.pojo.AlgorithmResult;
import com.zone24x7.ibrac.recengine.pojo.Product;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.recommendation.curator.RecommendedProductsCurator;
import com.zone24x7.ibrac.recengine.util.CustomReflectionTestUtils;
import com.zone24x7.ibrac.recengine.util.StringConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.time.ZoneId;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

/**
 * Test class of RecAlgorithmResultGenerator
 */
class RecAlgorithmResultGeneratorTest {
    private Logger logger;
    private RecAlgorithmResultGenerator recAlgorithmResultGenerator;
    private DatasourceAdapter datasourceAdapter;
    private RecommendedProductsCurator recommendedProductsCurator;
    private RecCycleStatus recCycleStatus;
    private Map<String, String> ccpCombinationMap;

    private static final String ALGO_ID = "100";
    private static final String REQUEST_ID = "-1314037956-1973208397";

    /**
     * Method to setup the dependencies for the test class
     *
     * @throws Exception if an error occurs when setting field value.
     */
    @BeforeEach
    void setup() throws Exception {
        datasourceAdapter = mock(DatasourceAdapter.class);
        recommendedProductsCurator = mock(RecommendedProductsCurator.class);
        recCycleStatus = mock(RecCycleStatus.class);
        logger = mock(Logger.class);

        ccpCombinationMap = new HashMap<>();
        recAlgorithmResultGenerator = new RecAlgorithmResultGenerator();

        when(recCycleStatus.getRequestId()).thenReturn(REQUEST_ID);

        Field loggerField = recAlgorithmResultGenerator.getClass().getDeclaredField("LOGGER");
        CustomReflectionTestUtils.setFinalStaticField(loggerField, this.logger);

        ReflectionTestUtils.setField(recAlgorithmResultGenerator, "datasourceAdapter", datasourceAdapter);
        ReflectionTestUtils.setField(recAlgorithmResultGenerator, "recommendedProductsCurator", recommendedProductsCurator);
        ReflectionTestUtils.setField(recAlgorithmResultGenerator, "recEngineTimezone", ZoneId.of("Asia/Colombo"));
    }

    /**
     * Test to verify an empty algo result is returned if a base connection error occurs.
     *
     * @throws BaseConnectionException if a base error occurs.
     */
    @Test
    public void should_return_an_empty_algorithm_result_if_an_error_occurs_when_getting_products_from_hbase() throws BaseConnectionException {
        when(datasourceAdapter.getResult(ALGO_ID, ccpCombinationMap, recCycleStatus)).thenThrow(mock(BaseConnectionException.class));

        AlgorithmResult algorithmResult = recAlgorithmResultGenerator.getAlgorithmResult(ALGO_ID, ccpCombinationMap, recCycleStatus);

        verify(recCycleStatus, times(1)).indicateExceptionInCallingHBase();
        verify(recommendedProductsCurator, never()).getProducts(anyList(), any(), any());
        assertThat(algorithmResult.getAlgorithmId(), is("-1"));
        assertThat(algorithmResult.getRecProducts(), empty());
        assertThat(algorithmResult.getUsedCcp(), anEmptyMap());

        verify(logger, times(1)).error(eq(StringConstants.REQUEST_ID_LOG_MSG_PREFIX + "HBase call failed. Algo:{}, ccp:{}"), eq(REQUEST_ID), eq(ALGO_ID), eq(ccpCombinationMap), any());
    }

    /**
     * Test to verify algo result is returned with curated products as expected.
     *
     * @throws BaseConnectionException if a base exception occurs.
     */
    @Test
    public void should_return_algo_result_with_curated_products_as_expected() throws BaseConnectionException {
        List<String> productList = Arrays.asList("100001", "100002");
        ccpCombinationMap.put("department", "Clothing");

        when(datasourceAdapter.getResult(ALGO_ID, ccpCombinationMap, recCycleStatus)).thenReturn(productList);
        when(recommendedProductsCurator.getProducts(eq(productList), any(), eq(recCycleStatus))).thenReturn(Collections.singletonList(new Product()));

        AlgorithmResult algorithmResult = recAlgorithmResultGenerator.getAlgorithmResult(ALGO_ID, ccpCombinationMap, recCycleStatus);

        verify(logger, never()).error(anyString());
        assertThat(algorithmResult.getAlgorithmId(), is(ALGO_ID));
        assertThat(algorithmResult.getRecProducts(), hasSize(1));
        assertThat(algorithmResult.getUsedCcp(), hasKey("department"));
    }

    /**
     * Test to verify algo result is returned with curated products as expected.
     *
     * @throws BaseConnectionException if a base exception occurs.
     */
    @Test
    public void should_return_algo_result_with_empty_products_if_curated_product_returns_empty_list() throws BaseConnectionException {
        List<String> productList = Arrays.asList("100001", "100002");
        ccpCombinationMap.put("department", "Clothing");

        when(datasourceAdapter.getResult(ALGO_ID, ccpCombinationMap, recCycleStatus)).thenReturn(productList);
        when(recommendedProductsCurator.getProducts(eq(productList), any(), eq(recCycleStatus))).thenReturn(Collections.emptyList());

        AlgorithmResult algorithmResult = recAlgorithmResultGenerator.getAlgorithmResult(ALGO_ID, ccpCombinationMap, recCycleStatus);

        verify(logger, never()).error(anyString());
        assertThat(algorithmResult.getAlgorithmId(), is(ALGO_ID));
        assertThat(algorithmResult.getRecProducts(), empty());
        assertThat(algorithmResult.getUsedCcp(), hasKey("department"));
    }
}