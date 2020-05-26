package com.zone24x7.ibrac.recengine.dao;

import com.zone24x7.ibrac.recengine.exceptions.BaseConnectionException;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import org.apache.hadoop.hbase.client.Result;

/**
 * Interface for HBase data access layer.
 */
public interface HBaseDao {
    /**
     * Method to get the hbase result for a given key, table, column family and qualifier.
     *
     * @param key            the key to retrieve the result
     * @param tableName      the table to retrieve the result
     * @param columnFamily   the column family to retrieve the result
     * @param qualifier      the qualifier to retrieve the result
     * @param recCycleStatus the recCycleStatus to retrieve the result
     * @return the result from hbase
     */
    Result getResult(String key, String tableName, String columnFamily, String qualifier, RecCycleStatus recCycleStatus) throws BaseConnectionException;
}
