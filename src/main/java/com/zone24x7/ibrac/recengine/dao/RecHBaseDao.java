package com.zone24x7.ibrac.recengine.dao;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Class to represent HBase DAO implementation.
 */
@Component
public class RecHBaseDao implements HBaseDao {

    @Autowired
    private HBaseConnection hBaseConnection;

    /**
     * Method to get the hbase result for a given key, table, column family and qualifier.
     *
     * @param key          the key to retrieve the result
     * @param tableName    the table to retrieve the result
     * @param columnFamily the column family to retrieve the result
     * @param qualifier    the qualifier to retrieve the result
     * @return the result from hbase
     */
    @Override
    public Result getResult(String key, String tableName, String columnFamily, String qualifier) {
        Connection connection = hBaseConnection.getConnection();
        Get get = new Get(key.getBytes());
        get.addColumn(columnFamily.getBytes(), qualifier.getBytes());
        Table table = null;
        try {
            table = connection.getTable(TableName.valueOf(tableName.getBytes()));
            Result result = table.get(get);
            table.close();
            return result;
        } catch (IOException e) {
            //TODO Handle exception
        }
        return null;
    }
}
