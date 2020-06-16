package com.zone24x7.ibrac.recengine.dao;

import com.zone24x7.ibrac.recengine.HBaseConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Class to test HBaseAdapterTest.
 */
@Disabled
public class HBaseConnectionTest {

    private HBaseConnection hBaseConnection;
    private HBaseConfig hBaseConfig;

    /**
     * Setup mock classes
     */
    @BeforeEach
    void setUp() throws Exception {
        hBaseConfig = new HBaseConfig();
        hBaseConfig.setZookeeperQuorum("10.101.16.82");
        hBaseConfig.setZookeeperPropertyClientPort("2181");
        hBaseConfig.setZookeeperZnodeParent("/hbase");
    }

    /**
     * Test to verify that hbase connection creation.
     */
    @Test
    public void should_create_the_hbase_connection_without_errors() throws UnsupportedOperationException, IOException {
        hBaseConnection = new HBaseConnection(hBaseConfig);
        hBaseConnection.getConnection();
    }
}
