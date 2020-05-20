package com.zone24x7.ibrac.recengine.pojo.tableconfigs;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.LinkedList;
import java.util.List;

/**
 * POJO class to represent column family.
 *
 */
public class ColumnFamily {

    @JsonProperty("family_name")
    private String familyName;

    @JsonProperty("columns")
    private List<Column> columns = new LinkedList<>();

    /**
     * Method to get column family name.
     *
     * @return Column family name.
     */
    @JsonProperty("family_name")
    public String getFamilyName() {
        return familyName;
    }

    /**
     * Method to set column family name.
     *
     * @param familyName Column family name to set.
     */
    @JsonProperty("family_name")
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    /**
     * Method to get list of columns.
     *
     * @return List of columns.
     */
    @JsonProperty("columns")
    public List<Column> getColumns() {
        return columns;
    }

    /**
     * Method to set columns.
     *
     * @param columns List of columns to set.
     */
    @JsonProperty("columns")
    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

}
