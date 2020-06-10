package com.zone24x7.ibrac.recengine.pojo.tableconfigs;

/**
 * POJO class to represent Table configuration information.
 */
public class TableConfigInfo {

    private String tableName;
    private String columnFamily;
    private String qualifier;
    private String description;
    private static final int HASH_SEED = 31;


    /**
     * Method to get table name.
     *
     * @return Table name.
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Method to set table name.
     *
     * @param tableName Table name to set.
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Method to get qualifier family.
     *
     * @return Column family.
     */
    public String getColumnFamily() {
        return columnFamily;
    }

    /**
     * Method to set qualifier family.
     *
     * @param columnFamily Column family to set.
     */
    public void setColumnFamily(String columnFamily) {
        this.columnFamily = columnFamily;
    }

    /**
     * Method to get qualifier.
     *
     * @return Column name.
     */
    public String getQualifier() {
        return qualifier;
    }

    /**
     * Method to set qualifier.
     *
     * @param qualifier Column to set.
     */
    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }


    /**
     * Method to get qualifier family.
     *
     * @return Column description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Method to set qualifier.
     *
     * @param description Column to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TableConfigInfo that = (TableConfigInfo) o;

        if (tableName != null ? !tableName.equals(that.tableName) : (that.tableName != null)) {
            return false;
        }
        if (columnFamily != null ? !columnFamily.equals(that.columnFamily) : (that.columnFamily != null)) {
            return false;
        }
        if (qualifier != null ? !qualifier.equals(that.qualifier) : (that.qualifier != null)) {
            return false;
        }
        return description != null ? description.equals(that.description) : (that.description == null);
    }

    @Override
    public int hashCode() {
        int result = tableName != null ? tableName.hashCode() : 0;
        result = HASH_SEED * result + (columnFamily != null ? columnFamily.hashCode() : 0);
        result = HASH_SEED * result + (qualifier != null ? qualifier.hashCode() : 0);
        result = HASH_SEED * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
