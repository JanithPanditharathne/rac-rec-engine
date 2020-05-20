package com.zone24x7.ibrac.recengine.pojo.tableconfigs;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.LinkedList;
import java.util.List;

/**
 * POJO class to represent EDE Tables.
 *
 */
public class TableConfigList {

    @JsonProperty("tables")
    private List<TableConfig> tables = new LinkedList<>();

    /**
     * Method to get list of tables.
     *
     * @return List of tables.
     */
    @JsonProperty("tables")
    public List<TableConfig> getTables() {
        return tables;
    }

    /**
     * Method to set table list.
     *
     * @param tables Table list to set.
     */
    @JsonProperty("tables")
    public void setTables(List<TableConfig> tables) {
        this.tables = tables;
    }
}
