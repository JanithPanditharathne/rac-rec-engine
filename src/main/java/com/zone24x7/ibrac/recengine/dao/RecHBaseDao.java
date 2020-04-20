package com.zone24x7.ibrac.recengine.dao;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RecHBaseDao implements HBaseDao {

    @Autowired
    private HBaseConnection hBaseConnection;

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
