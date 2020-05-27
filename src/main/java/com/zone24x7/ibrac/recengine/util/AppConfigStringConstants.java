package com.zone24x7.ibrac.recengine.util;

/**
 * Publicly used String constants values are stored here.
 */
public class AppConfigStringConstants {
    public static final String CONFIG_SYNC_INTERVAL = "${config.sync.interval}";
    public static final String CONFIG_SYNC_API_REC_SLOT = "${config.sync.api.recslot}";
    public static final String CONFIG_SYNC_API_REC = "${config.sync.api.recs}";
    public static final String CONFIG_SYNC_API_BUNDLE = "${config.sync.api.bundle}";
    public static final String CONFIG_SYNC_API_RULE = "${config.sync.api.rule}";
    public static final String CONFIG_SYNC_API_CALL_READ_TIMEOUT = "${config.sync.api.call.read.timeout}";
    public static final String CONFIG_SYNC_API_CALL_CONNECTION_TIMEOUT = "${config.sync.api.call.connection.timeout}";
    public static final String TABLE_CONFIG_FILE_NAME = "classpath:GetAllEdeTableConfigs.json";
    public static final String HBASE_CONNECTION_TIMEOUT = "${hbase.connection.timeout}";
    public static final String HBASE_CONNECTION_SKIP_COUNT = "${hbase.connection.skipcount}";
    public static final String HBASE_KEYMAKER_IGNORED_PARAMETERS = "#{'${hbase.keymaker.ignoredParameters}'.split(',')}";
    public static final String HBASE_RECCOMMENDATIONKEY_CCP_MULTI_VALUED_KEYS = "#{'${hbase.recommendationKey.ccp.multipleValued.keys}'.split(',')}";
}