package com.zone24x7.ibrac.recengine.converters;

import com.zone24x7.ibrac.recengine.exceptions.MalformedConfigurationException;
import com.zone24x7.ibrac.recengine.pojo.tableconfigs.*;
import com.zone24x7.ibrac.recengine.util.JsonPojoConverter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * Class to convert the tableConfig json to TableConfig map.
 */
@Component
public class TableConfigJsonToTableConfigMapConverter {

    /**
     * Method to convert table Json string to Table config info map.
     *
     * @param tableConfigJson Ede table Json to process.
     * @return Algorithm Id to Table config info map.
     */
    public Map<String, TableConfigInfo> convert(String tableConfigJson) throws MalformedConfigurationException {
        Map<String, TableConfigInfo> tableConfigInfoMap = new HashMap<>();
        TableConfigList tableConfigList = null;

        if (StringUtils.isEmpty(tableConfigJson)) {
            return tableConfigInfoMap;
        }

        try {
            tableConfigList = JsonPojoConverter.getTableConfigListsFromJson(tableConfigJson);
        } catch (IOException e) {
            throw new MalformedConfigurationException("Error decoding table configuration json: " + tableConfigJson, e);
        }

        if (tableConfigList != null) {
            List<TableConfig> tableConfigs = tableConfigList.getTables();

            if (CollectionUtils.isNotEmpty(tableConfigs)) {
                for (TableConfig tableConfig : tableConfigs) {
                    tableConfigInfoMap.putAll(processTableConfigList(tableConfig));
                }
            }
        }

        return tableConfigInfoMap;
    }

    /**
     * Method to process table config list.
     *
     * @param tableConfig Ede table to process.
     * @return Map<String   ,       TableConfigInfo> returns the map of TableConfigInfo.
     */
    private Map<String, TableConfigInfo> processTableConfigList(TableConfig tableConfig) {
        Map<String, TableConfigInfo> tableConfigInfoMap = new HashMap<>();
        String tableName = tableConfig.getTableName();
        List<ColumnFamily> columnFamilies = tableConfig.getColumnFamilies();

        if (CollectionUtils.isEmpty(columnFamilies)) {
            return tableConfigInfoMap;
        }

        for (ColumnFamily columnFamily : columnFamilies) {
            String familyName = columnFamily.getFamilyName();
            List<Column> columns = columnFamily.getColumns();

            if (CollectionUtils.isEmpty(columns)) {
                continue;
            }

            for (Column column : columns) {
                // Algorithm Id is the column name.
                String algorithmId = column.getColumnName();

                TableConfigInfo tableConfigInfo = new TableConfigInfo();
                tableConfigInfo.setQualifier(algorithmId);
                tableConfigInfo.setColumnFamily(familyName);
                tableConfigInfo.setTableName(tableName);
                tableConfigInfo.setDescription(column.getDescription());

                tableConfigInfoMap.put(algorithmId, tableConfigInfo);
            }
        }
        return tableConfigInfoMap;
    }
}
