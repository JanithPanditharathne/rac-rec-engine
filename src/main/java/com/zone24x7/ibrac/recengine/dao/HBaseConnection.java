package com.zone24x7.ibrac.recengine.dao;

import com.zone24x7.ibrac.recengine.HBaseConfig;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HBaseConnection {

    private Connection connection;

    public HBaseConnection( HBaseConfig hBaseConfig) throws IOException {
        Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", hBaseConfig.getZookeeperQuorum());
        config.set("hbase.zookeeper.property.clientPort", hBaseConfig.getZookeeperPropertyClientPort());
        config.set("zookeeper.znode.parent", hBaseConfig.getZookeeperZnodeParent());

        this.connection = ConnectionFactory.createConnection(config);
    }

    public Connection getConnection() {
        return connection;
    }
}
