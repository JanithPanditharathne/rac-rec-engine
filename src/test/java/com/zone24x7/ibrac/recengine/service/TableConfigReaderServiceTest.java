package com.zone24x7.ibrac.recengine.service;

import com.zone24x7.ibrac.recengine.pojo.tableconfigs.TableConfigInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Class to test the TableConfigReaderService.
 */
public class TableConfigReaderServiceTest {

    private TableConfigReaderService tableConfigReaderService;

    private Map<String, TableConfigInfo> tableConfigInfoMap = null;

    TableConfigInfo tableConfigInfo;

    /**
     * Method to setup the dependencies for the test class
     */
    @BeforeEach
    public void setup() {
        tableConfigReaderService = new TableConfigReaderService();

        tableConfigInfoMap = new HashMap<>();

        tableConfigInfo = new TableConfigInfo();
        tableConfigInfo.setQualifier("141");
        tableConfigInfo.setColumnFamily("rec");
        tableConfigInfo.setTableName("recommendations_trending");
        tableConfigInfo.setDescription("Trending Sesonal top K trending");

        tableConfigInfoMap.put("141", tableConfigInfo);

        ReflectionTestUtils.setField(tableConfigReaderService,"tableConfigurationMap",tableConfigInfoMap);
    }

    /**
     * Test to verify that correct tableConfigInfo object return for the given algorithm.
     *
     */
    @Test
    public void should_get_table_config_info_object_for_given_algorithm() {

        TableConfigInfo tableConfigInfo = tableConfigReaderService.getTableConfigInfoByAlgorithmId("141");
        assertEquals(this.tableConfigInfo,tableConfigInfo);

    }
}
