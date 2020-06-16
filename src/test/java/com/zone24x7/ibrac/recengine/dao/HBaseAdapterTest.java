package com.zone24x7.ibrac.recengine.dao;

import com.zone24x7.ibrac.recengine.exceptions.BaseConnectionException;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.pojo.tableconfigs.TableConfigInfo;
import com.zone24x7.ibrac.recengine.service.TableConfigReaderService;
import com.zone24x7.ibrac.recengine.util.CustomReflectionTestUtils;
import org.apache.hadoop.hbase.client.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Class to test HBaseAdapter.
 */
public class HBaseAdapterTest {

    private HBaseAdapter hBaseAdapter;
    private TableConfigReaderService tableConfigReaderService;
    private String algorithmName = "100";
    Map<String, String> ccpMap;
    private Logger logger;
    private String requestId;
    private HBaseDao hBaseDao;
    private String key = "9673386461547620374";

    private String tableName = "recommendations_trending";
    private String columnFamily = "rec";
    private String description = "Top Trending";
    Result res;
    TableConfigInfo tableConfigInfo;
    RecCycleStatus recCycleStatus;

    private static List<String> ignoredKeys;
    private static List<String> sortedKeys;

    /**
     * Setup mock classes
     */
    @BeforeEach
    void setUp() throws Exception {

        hBaseAdapter = new HBaseAdapter();
        logger = mock(Logger.class);
        hBaseDao = mock(HBaseDao.class);
        recCycleStatus = mock(RecCycleStatus.class);
        res = mock(Result.class);

        requestId = "7ff6a25b-2b47-4c59-8ef9-8010732ce7b3";

        recCycleStatus = new RecCycleStatus(requestId);

        ccpMap = new HashMap<>();
        ccpMap.put("productNumber", "2213222");

        // Fill the ignored keys list
        fill_ignored_keys_array();

        // Fill the sorted keys list
        fill_sorted_keys_array();

        // Set the lists to the HBaseKeyMaker.
        HBaseKeyMaker.setIgnoredKeys(ignoredKeys);
        HBaseKeyMaker.setSortedKeys(sortedKeys);

        Field loggerField = hBaseAdapter.getClass().getDeclaredField("LOGGER");
        CustomReflectionTestUtils.setFinalStaticField(loggerField, this.logger);

        // Mock the tableConfigReaderService.
        tableConfigReaderService = mock(TableConfigReaderService.class);

        //  Add the mocked tableConfigReaderService instance to the hBaseAdapter.
        ReflectionTestUtils.setField(hBaseAdapter, "tableConfigReaderService", tableConfigReaderService);

        //  Add the mocked hBaseDao instance to the hBaseAdapter.
        ReflectionTestUtils.setField(hBaseAdapter, "hBaseDao", hBaseDao);

        tableConfigInfo = new TableConfigInfo();
        tableConfigInfo.setTableName(tableName);
        tableConfigInfo.setColumnFamily(columnFamily);
        tableConfigInfo.setQualifier(algorithmName);
        tableConfigInfo.setDescription(description);
    }


    /**
     * Test to verify that UnsupportedOperationException is thrown when given algorithm id not exists.
     */
    @Test
    public void should_throw_base_connection_exception_if_provided_algorithm_id_not_exists() throws UnsupportedOperationException {

        when(tableConfigReaderService.getTableConfigInfoByAlgorithmId(algorithmName)).thenReturn(null);

        // Pass non-existing algorithm id to get the exception.

        assertThrows(UnsupportedOperationException.class, () -> {
            hBaseAdapter.getResult(algorithmName, ccpMap, recCycleStatus);
        });
    }


    /**
     * Test to verify that UnsupportedOperationException is thrown when given algorithm id not exists.
     */
    @Test
    public void should_get_accurate_result_when_correct_algo_id_provided() throws BaseConnectionException, IOException {

        List<String> result = new ArrayList<>();

        result.add("1000001");
        result.add("1000002");
        result.add("1000003");
        result.add("1000004");
        result.add("1000005");
        result.add("1000006");
        result.add("1000007");
        result.add("1000008");
        result.add("1000009");
        result.add("1000010");
        result.add("1000011");
        result.add("1000012");

        String productString = "1000001,1000002,1000003,1000004,1000005,1000006,1000007," +
                "1000008,1000009,1000010,1000011,1000012";

        byte[] productByte = productString.getBytes();

        RecCycleStatus recCycleStatus = mock(RecCycleStatus.class);

        when(tableConfigReaderService.getTableConfigInfoByAlgorithmId(algorithmName)).thenReturn(tableConfigInfo);

        when(hBaseDao.getResult(key, tableName, columnFamily, algorithmName, recCycleStatus)).thenReturn(res);

        when(res.getValue(columnFamily.getBytes(), algorithmName.getBytes())).thenReturn(productByte);

        List<String> resultedProducts = hBaseAdapter.getResult(algorithmName, ccpMap, recCycleStatus);

        assertThat(resultedProducts, is(equalTo(result)));
    }

    /**
     * Test to verify that empty list is return when dao layer returns null.
     */
    @Test
    public void should_get_empty_list_when_null_return_from_hbase() throws BaseConnectionException, IOException {

        RecCycleStatus recCycleStatus = mock(RecCycleStatus.class);

        when(tableConfigReaderService.getTableConfigInfoByAlgorithmId(algorithmName)).thenReturn(tableConfigInfo);

        when(hBaseDao.getResult(key, tableName, columnFamily, algorithmName, recCycleStatus)).thenReturn(res);

        when(res.getValue(columnFamily.getBytes(), algorithmName.getBytes())).thenReturn(null);

        List<String> resultedProducts = hBaseAdapter.getResult(algorithmName, ccpMap, recCycleStatus);

        assertThat(resultedProducts, is(equalTo(Collections.emptyList())));

    }

    /**
     * Test to verify that empty list is return when dao layer returns null.
     */
    @Test
    public void should_get_empty_list_when_no_bytes_return_from_hbase() throws BaseConnectionException, IOException {

        RecCycleStatus recCycleStatus = mock(RecCycleStatus.class);

        when(tableConfigReaderService.getTableConfigInfoByAlgorithmId(algorithmName)).thenReturn(tableConfigInfo);

        when(hBaseDao.getResult(key, tableName, columnFamily, algorithmName, recCycleStatus)).thenReturn(res);

        when(res.getValue(columnFamily.getBytes(), algorithmName.getBytes())).thenReturn(new byte[0]);

        List<String> resultedProducts = hBaseAdapter.getResult(algorithmName, ccpMap, recCycleStatus);

        assertThat(resultedProducts, is(equalTo(Collections.emptyList())));

    }


    /**
     * Fill the ignoredKeys array list
     */
    private void fill_ignored_keys_array() {

        ignoredKeys = new ArrayList<>();

        ignoredKeys.add("shipNodes");
        ignoredKeys.add("shipNode");
    }

    /**
     * Fill the sortedKeys array list
     */
    private void fill_sorted_keys_array() {

        sortedKeys = new ArrayList<>();

        sortedKeys.add("productNumbers");
        sortedKeys.add("permutedProductNumbers");
        sortedKeys.add("skuNumbers");
    }
}
