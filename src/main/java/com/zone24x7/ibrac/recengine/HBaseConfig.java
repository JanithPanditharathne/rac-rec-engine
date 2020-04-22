package com.zone24x7.ibrac.recengine;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for injecting HBase related configs.
 */
@Configuration
@ConfigurationProperties(prefix = "hbase")
public class HBaseConfig {
    private String zookeeperQuorum;
    private String zookeeperPropertyClientPort;
    private String zookeeperZnodeParent;

    /**
     * Returns the zookeeper quorum.
     *
     * @return zookeeper quorum.
     */
    public String getZookeeperQuorum() {
        return zookeeperQuorum;
    }

    /**
     * Sets the zookeeper quorum.
     *
     * @param zookeeperQuorum zookeeperQuorum to set.
     */
    public void setZookeeperQuorum(String zookeeperQuorum) {
        this.zookeeperQuorum = zookeeperQuorum;
    }

    /**
     * Returns the zookeeper client port.
     *
     * @return the zookeeper client port.
     */
    public String getZookeeperPropertyClientPort() {
        return zookeeperPropertyClientPort;
    }

    /**
     * Sets the  zookeeper client port.
     *
     * @param zookeeperPropertyClientPort the zookeeper client port to set.
     */
    public void setZookeeperPropertyClientPort(String zookeeperPropertyClientPort) {
        this.zookeeperPropertyClientPort = zookeeperPropertyClientPort;
    }

    /**
     * Gets the Znode parent
     *
     * @return the Znode parent
     */
    public String getZookeeperZnodeParent() {
        return zookeeperZnodeParent;
    }

    /**
     * Sets the zookeeperZnodeParent
     *
     * @param zookeeperZnodeParent zookeeperZnodeParent to set
     */
    public void setZookeeperZnodeParent(String zookeeperZnodeParent) {
        this.zookeeperZnodeParent = zookeeperZnodeParent;
    }
}
