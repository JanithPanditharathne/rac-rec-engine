package com.zone24x7.ibrac.recengine.dao;

import com.zone24x7.ibrac.recengine.service.TableConfigReaderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Class to test HBaseAdapterTest.
 */
public class HBaseAdapterTest {

    private HBaseAdapter hBaseAdapter;

    Map<String, String> testMap = new HashMap<>();
    private TableConfigReaderService tableConfigReaderService;
    private String algorithName = "1001";

    /**
     * Setup mock classes
     */
    @BeforeEach
    void setUp() {

        hBaseAdapter = new HBaseAdapter();

        // Mock the tableConfigReaderService.
        tableConfigReaderService = mock(TableConfigReaderService.class);

        //  Add the mocked instance to the hBaseAdapter.
        ReflectionTestUtils.setField(hBaseAdapter, "tableConfigReaderService", tableConfigReaderService);

        // Put values to the map
        testMap.put("department", "Clothing");
    }

    /**
     * Test to verify that HBaseAdapterException is thrown when given algorithm id not exists.
     */
  /*  @Test
    public void should_throw_hbase_adapter_exception_if_provided_algorithm_id_not_exists() throws HBaseAdapterException {

        when(tableConfigReaderService.getTableConfigInfoByAlgorithmId(algorithName)).thenReturn(null);

        // Pass non-existing algorithm id to get the exception.
        assertThrows(HBaseAdapterException.class, () -> hBaseAdapter.getResult("1001", testMap));

    }*/
}
