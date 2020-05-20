package com.zone24x7.ibrac.recengine.converters;

import com.zone24x7.ibrac.recengine.exceptions.MalformedConfigurationException;
import com.zone24x7.ibrac.recengine.pojo.tableconfigs.TableConfigInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TableConfigJsonToTableConfigMapConverterTest {

    private TableConfigJsonToTableConfigMapConverter tableConfigJsonToTableConfigMapConverter;

    Map<String, TableConfigInfo> emptyTableConfigInfoMap;

    Map<String, TableConfigInfo> tableConfigInfoMap;

    private static final String MALFORMED_TABLE_CONFIG_JSON = "{";

    private static final String NO_COLUMN_FAMILIES_IN_TABLE_CONFIG_JSON = "{\"tables\": [{\"table_name\": \"recommendations_trending\",\"column_families\": []}]}";

    private static final String NO_TABLES_IN_TABLE_CONFIG_JSON = "{\"tables\": []}";

    private static final String CORRECT_TABLE_CONFIG_JSON = "{\"tables\": [{\"table_name\": \"recommendations_trending\",\"column_families\": [{\"family_name\": \"rec\",\"columns\": [{\"column_name\": \"141\",\"description\": \"Trending Sesonal top K trending\",\"data_path\": \"/staged/seasonal_trending/view_trendingProductsOutput/0/consolidated_ede\"}]}]}]}";

    private static final String TABLE_CONFIG_JSON_WITH_EMPTY_COLUMNS = "{\"tables\": [{\"table_name\": \"recommendations_trending\",\"column_families\": [{\"family_name\": \"rec\",\"columns\": []}]}]}";


    /**
     * Setup mock classes
     */
    @BeforeEach
    void setUp() {

        tableConfigJsonToTableConfigMapConverter = new TableConfigJsonToTableConfigMapConverter();
        emptyTableConfigInfoMap = new HashMap<>();
        tableConfigInfoMap = new HashMap<>();

        TableConfigInfo tableConfigInfo = new TableConfigInfo();
        tableConfigInfo.setQualifier("141");
        tableConfigInfo.setColumnFamily("rec");
        tableConfigInfo.setTableName("recommendations_trending");
        tableConfigInfo.setDescription("Trending Sesonal top K trending");

        tableConfigInfoMap.put("141", tableConfigInfo);
    }


    /**
     * Test to verify that correct tableConfigInfoMap is return when correct table config json is passed.
     */
    @Test
    public void should_return_matching_table_config_map_when_well_formed_table_config_string_is_passed() throws MalformedConfigurationException {

        Map<String, TableConfigInfo> tableConfigInfoMapResult = tableConfigJsonToTableConfigMapConverter.convert(CORRECT_TABLE_CONFIG_JSON);
        assertEquals(tableConfigInfoMap, tableConfigInfoMapResult);
    }

    /**
     * Test to verify that new tableConfigInfoMap is return when table config json with empty columns is passed.
     */
    @Test
    public void should_return_new_table_config_map_when_table_config_string_with_empty_columns_is_passed() throws MalformedConfigurationException {

        Map<String, TableConfigInfo> tableConfigInfoMapResult = tableConfigJsonToTableConfigMapConverter.convert(TABLE_CONFIG_JSON_WITH_EMPTY_COLUMNS);
        assertEquals(emptyTableConfigInfoMap, tableConfigInfoMapResult);
    }

    /**
     * Test to verify that MalformedConfiguration exception is thrown when table config json is malformed.
     */
    @Test
    public void should_throw_an_exception_if_table_config_string_is_malformed() {

        assertThrows(MalformedConfigurationException.class, () -> tableConfigJsonToTableConfigMapConverter.convert(MALFORMED_TABLE_CONFIG_JSON));
    }


    /**
     * Test to verify that new  emptyTableConfigInfoMap is return when table config json is empty.
     */
    @Test
    public void should_return_new_table_config_map_when_table_config_string_is_empty_string() throws MalformedConfigurationException {

        Map<String, TableConfigInfo> tableConfigInfoMapResult = tableConfigJsonToTableConfigMapConverter.convert("");
        assertEquals(emptyTableConfigInfoMap, tableConfigInfoMapResult);
    }


    /**
     * Test to verify that new  emptyTableConfigInfoMap is return when no tables in table config json.
     */
    @Test
    public void should_return_new_table_config_map_when_no_tables_in_table_config_string() throws MalformedConfigurationException {

        Map<String, TableConfigInfo> tableConfigInfoMapResult = tableConfigJsonToTableConfigMapConverter.convert(NO_TABLES_IN_TABLE_CONFIG_JSON);
        assertEquals(emptyTableConfigInfoMap, tableConfigInfoMapResult);
    }


    /**
     * Test to verify that new  emptyTableConfigInfoMap is return when no column families in table config json.
     */
    @Test
    public void should_return_new_table_config_map_when_no_column_families_in_table_config_string() throws MalformedConfigurationException {

        Map<String, TableConfigInfo> tableConfigInfoMapResult = tableConfigJsonToTableConfigMapConverter.convert(NO_COLUMN_FAMILIES_IN_TABLE_CONFIG_JSON);
        assertEquals(emptyTableConfigInfoMap, tableConfigInfoMapResult);
    }
}
