package com.zone24x7.ibrac.recengine.dao;

import com.zone24x7.ibrac.recengine.exceptions.BaseConnectionException;
import com.zone24x7.ibrac.recengine.pojo.RecCycleStatus;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.concurrent.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


/**
 * Class to test RecHBaseDao.
 */
public class RecHBaseDaoTest {

    private HBaseConnection hBaseConnection;
    private RecHBaseDao recHBaseDao;
    private RecCycleStatus recCycleStatus;
    private ExecutorService cachedTaskExecutorService;
    private Connection connection;
    private int hBaseConnectionTimeout;
    private Future<Result> future;
    private Result recResult;
    private Table table;

    private String key = "9673386461547620374";
    private String tableName = "recommendations_trending";
    private String columnFamily = "rec";
    private String algorithmName = "100";

    /**
     * Setup mock classes
     */
    @BeforeEach
    void setUp() throws Exception {

        hBaseConnection = mock(HBaseConnection.class);

        recCycleStatus = mock(RecCycleStatus.class);

        connection = mock(Connection.class);

        table = mock(Table.class);

        cachedTaskExecutorService = mock(ExecutorService.class);

        future = mock(Future.class);

        recResult = mock(Result.class);

        hBaseConnectionTimeout = 2000;

        recHBaseDao = new RecHBaseDao(2);

        ReflectionTestUtils.setField(recHBaseDao, "hBaseConnection", hBaseConnection);
        ReflectionTestUtils.setField(recHBaseDao, "cachedTaskExecutorService", cachedTaskExecutorService);
        ReflectionTestUtils.setField(recHBaseDao, "hBaseConnectionTimeout", hBaseConnectionTimeout);

    }


    /**
     * Test to verify that the correct Reust returned when no interruption occurs.
     */
    @Test
    public void should_return_correct_result_when_no_interruption_occurs() throws InterruptedException, ExecutionException, TimeoutException, IOException, BaseConnectionException {

        when(hBaseConnection.getConnection()).thenReturn(connection);

        when(connection.getTable(TableName.valueOf(tableName.getBytes()))).thenReturn(table);

        when(table.get(mock(Get.class))).thenReturn(recResult);

        when(cachedTaskExecutorService.submit(any(Callable.class))).thenReturn(future);

        when(future.get(hBaseConnectionTimeout, TimeUnit.MILLISECONDS)).thenReturn(recResult);

        Result res = recHBaseDao.getResult(key, tableName, columnFamily, algorithmName, recCycleStatus);

        assertThat(res, is(equalTo(recResult)));
    }


    /**
     * Test to verify that the BaseConnectionException throws when connection is not available.
     */
    @Test
    public void should_throw_base_connection_exception_when_connection_is_null() throws BaseConnectionException {

        when(hBaseConnection.getConnection()).thenReturn(null);

        assertThrows(BaseConnectionException.class, () -> {
            recHBaseDao.getResult(key, tableName, columnFamily, algorithmName, recCycleStatus);
        });

    }


    /**
     * Test to verify that the InterruptedException throws when hbase is offline.
     */
    @Test
    public void should_throw_interrupt_exception_thrown_when_hbase_is_offline() throws InterruptedException, ExecutionException, TimeoutException, IOException {

        when(hBaseConnection.getConnection()).thenReturn(connection);

        when(connection.getTable(TableName.valueOf(tableName.getBytes()))).thenReturn(table);

        when(table.get(mock(Get.class))).thenReturn(recResult);

        when(cachedTaskExecutorService.submit(any(Callable.class))).thenReturn(future);

        when(future.get(hBaseConnectionTimeout, TimeUnit.MILLISECONDS)).thenThrow(new InterruptedException());

        assertThrows(BaseConnectionException.class, () -> {
            recHBaseDao.getResult(key, tableName, columnFamily, algorithmName, recCycleStatus);
        });

    }

    /**
     * Test to verify that the ExecutionException throws when hbase is offline.
     */
    @Test
    public void should_throw_execution_exception_thrown_when_hbase_is_offline() throws InterruptedException, ExecutionException, TimeoutException, IOException {

        when(hBaseConnection.getConnection()).thenReturn(connection);

        when(connection.getTable(TableName.valueOf(tableName.getBytes()))).thenReturn(table);

        when(table.get(mock(Get.class))).thenReturn(recResult);

        when(cachedTaskExecutorService.submit(any(Callable.class))).thenReturn(future);

        when(future.get(hBaseConnectionTimeout, TimeUnit.MILLISECONDS)).thenThrow(new ExecutionException(new Throwable()));

        assertThrows(BaseConnectionException.class, () -> {
            recHBaseDao.getResult(key, tableName, columnFamily, algorithmName, recCycleStatus);
        });

    }

    /**
     * Test to verify that the TimeoutException throws when hbase is offline.
     */
    @Test
    public void should_throw_timeout_exception_thrown_when_hbase_is_offline() throws InterruptedException, ExecutionException, TimeoutException, IOException {

        when(hBaseConnection.getConnection()).thenReturn(connection);

        when(connection.getTable(TableName.valueOf(tableName.getBytes()))).thenReturn(table);

        when(table.get(mock(Get.class))).thenReturn(recResult);

        when(cachedTaskExecutorService.submit(any(Callable.class))).thenReturn(future);

        when(future.get(hBaseConnectionTimeout, TimeUnit.MILLISECONDS)).thenThrow(new TimeoutException());

        assertThrows(BaseConnectionException.class, () -> {
            recHBaseDao.getResult(key, tableName, columnFamily, algorithmName, recCycleStatus);
        });

    }
}