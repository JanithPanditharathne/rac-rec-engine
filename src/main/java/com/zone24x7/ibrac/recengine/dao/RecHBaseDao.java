package com.zone24x7.ibrac.recengine.dao;

import com.zone24x7.ibrac.recengine.exceptions.BaseConnectionException;
import com.zone24x7.ibrac.recengine.logging.Log;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import com.zone24x7.ibrac.recengine.util.AppConfigStringConstants;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class to represent HBase DAO implementation.
 */
@Component
public class RecHBaseDao implements HBaseDao {

    @Autowired
    private HBaseConnection hBaseConnection;

    @Log
    private static Logger logger;

    @Autowired
    @Qualifier("cachedThreadPoolTaskExecutor")
    private ExecutorService cachedTaskExecutorService;

    @Value(AppConfigStringConstants.HBASE_CONNECTION_TIMEOUT)
    private Integer hBaseConnectionTimeout;


    /*
        Following variables are used to maintain HBase state and to control the number of threads that gets blocked
        in an HBase offline situation
     */
    private AtomicBoolean isHBaseOnline;
    private AtomicInteger currentHBaseConnectionSkipCount;
    private Integer hBaseConnectionSkipCount;

    private static final String HBASE_CONNECTION_FAILURE_MSG = "Could not connect to Hbase";
    private static final String HBASE_CONNECTION_RETRY_SKIPPED = "Connection retry skipped due to exceeding skip count.";
    private static final String HBASE_CONNECTION_TIMEOUT_EXP_MSG = "Hbase call timed out";

    /**
     * Constructor to instantiate RecHBaseDao.
     *
     * @param skipCount the skip count
     */
    public RecHBaseDao(@Value(AppConfigStringConstants.HBASE_CONNECTION_SKIP_COUNT) Integer skipCount) {
        this.isHBaseOnline = new AtomicBoolean(true);
        this.hBaseConnectionSkipCount = skipCount;
        this.currentHBaseConnectionSkipCount = new AtomicInteger(skipCount);
    }

    /**
     * Method to get the hbase result for a given key, table, column family and qualifier.
     *
     * @param key               the key to retrieve the result
     * @param tableName         the table to retrieve the result
     * @param columnFamily      the column family to retrieve the result
     * @param qualifier         the qualifier to retrieve the result
     * @param recCycleStatus    the recCycleStatus to retrieve the result
     * @return the result from hbase
     */
    @Override
    public Result getResult(String key, String tableName, String columnFamily, String qualifier, RecCycleStatus recCycleStatus) throws BaseConnectionException {
        Connection connection = hBaseConnection.getConnection();

        if (connection != null && (isHBaseOnline.get() || isHBaseSkipCountReached())) {
            Future<Result> future = null;

            try {
                future = cachedTaskExecutorService.submit(() -> {
                    Get get = new Get(key.getBytes());
                    get.addColumn(columnFamily.getBytes(), qualifier.getBytes());
                    Table table = connection.getTable(TableName.valueOf(tableName.getBytes()));
                    Result result = table.get(get);
                    table.close();
                    return result;

                });

                Result result = future.get(hBaseConnectionTimeout, TimeUnit.MILLISECONDS);
                isHBaseOnline.set(true);
                return result;

            } catch (InterruptedException e) {
                isHBaseOnline.set(false);
                Thread.currentThread().interrupt();
                throw new BaseConnectionException(HBASE_CONNECTION_FAILURE_MSG, e);
            } catch (ExecutionException e) {
                isHBaseOnline.set(false);
                throw new BaseConnectionException(HBASE_CONNECTION_FAILURE_MSG, e);
            } catch (TimeoutException te) {
                isHBaseOnline.set(false);
                cancelFuture(future);
                throw new BaseConnectionException(HBASE_CONNECTION_TIMEOUT_EXP_MSG, te);
            }
        } else {
            throw new BaseConnectionException(HBASE_CONNECTION_RETRY_SKIPPED);
        }
    }

    /**
     * Method to check whether HBase skip count is reached.
     *
     * @return true if skip count reached, else false
     */
    private boolean isHBaseSkipCountReached() {
        return (!isHBaseOnline.get() && currentHBaseConnectionSkipCount.updateAndGet(x -> x <= 0 ? hBaseConnectionSkipCount : (x - 1)) == hBaseConnectionSkipCount);
    }

    /**
     * Method to cancel a future.
     *
     * @param future the future to cancel
     */
    private static void cancelFuture(Future future) {
        if (future != null) {
            future.cancel(true);
        }
    }
}
