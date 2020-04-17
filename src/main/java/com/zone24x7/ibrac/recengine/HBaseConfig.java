package com.zone24x7.ibrac.recengine;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "hbase")
public class HBaseConfig {
    private String zookeeperQuorum;
    private String zookeeperPropertyClientPort;
    private String zookeeperZnodeParent;

    public String getZookeeperQuorum() {
        return zookeeperQuorum;
    }

    public void setZookeeperQuorum(String zookeeperQuorum) {
        this.zookeeperQuorum = zookeeperQuorum;
    }

    public String getZookeeperPropertyClientPort() {
        return zookeeperPropertyClientPort;
    }

    public void setZookeeperPropertyClientPort(String zookeeperPropertyClientPort) {
        this.zookeeperPropertyClientPort = zookeeperPropertyClientPort;
    }

    public String getZookeeperZnodeParent() {
        return zookeeperZnodeParent;
    }

    public void setZookeeperZnodeParent(String zookeeperZnodeParent) {
        this.zookeeperZnodeParent = zookeeperZnodeParent;
    }
}
