package com.zone24x7.ibrac.recengine.util;

/**
 * Publicly used String constants values are stored here.
 */
public class AppConfigStringConstants {
    public static final String CONFIG_SYNC_STRATEGY = "${config.sync.strategy}";
    public static final String CONFIG_SYNC_INTERVAL = "${config.sync.interval}";
    public static final String CONFIG_SYNC_API_REC_SLOT = "${config.sync.api.recslot}";
    public static final String CONFIG_SYNC_API_REC = "${config.sync.api.recs}";
    public static final String CONFIG_SYNC_API_BUNDLE = "${config.sync.api.bundle}";
    public static final String CONFIG_SYNC_API_RULE = "${config.sync.api.rule}";
    public static final String CONFIG_SYNC_API_CALL_READ_TIMEOUT = "${config.sync.api.call.read.timeout}";
    public static final String CONFIG_SYNC_API_CALL_CONNECTION_TIMEOUT = "${config.sync.api.call.connection.timeout}";

    public static final String CONFIG_SYNC_BACKUP_RECSLOTS_CONFIG = "${config.backup.recslots}";
    public static final String CONFIG_SYNC_BACKUP_RECS_CONFIG = "${config.backup.recs}";
    public static final String CONFIG_SYNC_BACKUP_BUNDLES_CONFIG = "${config.backup.bundles}";
    public static final String CONFIG_SYNC_BACKUP_RULES_CONFIG = "${config.backup.rules}";

    public static final String CONFIG_RESOURCE_CLASSPATH_ALGO_PARAMS = "${resource.file.classpath.algoParams}";
    public static final String RULE_TRANSLATOR_ATTRIBUTE_MAPPING = "#{${ruleTranslator.attributeMapping}}";

    public static final String CONFIG_REC_RESPONSE_CURRENCY = "${recController.response.currency}";
    public static final String CONFIG_REC_RESPONSE_IMAGE_WIDTH = "${recController.response.image.width}";
    public static final String CONFIG_REC_RESPONSE_IMAGE_HEIGHT= "${recController.response.image.height}";

    /**
     * Private constructor to stop initiation
     */
    private AppConfigStringConstants() {
        throw new IllegalStateException("AppConfigStringConstants is a utility class");
    }
}