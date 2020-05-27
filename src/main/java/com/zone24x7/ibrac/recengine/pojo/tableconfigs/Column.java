package com.zone24x7.ibrac.recengine.pojo.tableconfigs;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO class to represent column.
 *
 */
public class Column {

    @JsonProperty("column_name")
    private String columnName;

    @JsonProperty("description")
    private String description;

    @JsonProperty("data_path")
    private String dataPath;

    /**
     * Method to get column name.
     *
     * @return Column name.
     */
    @JsonProperty("column_name")
    public String getColumnName() {
        return columnName;
    }

    /**
     * Method to set column name.
     *
     * @param columnName Column name to set.
     */
    @JsonProperty("column_name")
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * Method to get description.
     *
     * @return Description.
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * Method to set description.
     *
     * @param description Description to set.
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Method to get data path.
     *
     * @return Data path.
     */
    @JsonProperty("data_path")
    public String getDataPath() {
        return dataPath;
    }

    /**
     * Method to set data path.
     *
     * @param dataPath Data path to set.
     */
    @JsonProperty("data_path")
    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }
}
