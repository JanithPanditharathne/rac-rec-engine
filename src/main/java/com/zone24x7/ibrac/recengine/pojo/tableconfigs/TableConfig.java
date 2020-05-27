package com.zone24x7.ibrac.recengine.pojo.tableconfigs;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.LinkedList;
import java.util.List;

/**
 * POJO class to represent EDE table.
 *
 */
public class TableConfig {

    @JsonProperty("table_name")
    private String tableName;

    @JsonProperty("column_families")
    private List<ColumnFamily> columnFamilies = new LinkedList<>();

    /**
     * Method to get table name.
     *
     * @return Table name.
     */
    @JsonProperty("table_name")
    public String getTableName() {
        return tableName;
    }

    /**
     * Method to set table name.
     *
     * @param tableName Table name to set.
     */
    @JsonProperty("table_name")
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Method to get column family list.
     *
     * @return Column family list.
     */
    @JsonProperty("column_families")
    public List<ColumnFamily> getColumnFamilies() {
        return columnFamilies;
    }

    /**
     * Method to set column family list.
     *
     * @param columnFamilies Column family list to set.
     */
    @JsonProperty("column_families")
    public void setColumnFamilies(List<ColumnFamily> columnFamilies) {
        this.columnFamilies = columnFamilies;
    }
}
