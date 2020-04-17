package com.zone24x7.ibrac.recengine.dao;

import org.apache.hadoop.hbase.client.Result;

public interface HBaseDao {
    Result getResult(String key, String table, String columnFamily, String qualifier);
}
