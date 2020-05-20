package com.zone24x7.ibrac.recengine;

import com.zone24x7.ibrac.recengine.configuration.fetch.CsConfigurationsFetchApiCallStrategy;
import com.zone24x7.ibrac.recengine.configuration.fetch.CsConfigurationsFetchLocalConfigStrategy;
import com.zone24x7.ibrac.recengine.configuration.fetch.CsConfigurationsFetchStrategy;
import com.zone24x7.ibrac.recengine.configuration.sync.CsConfiguration;
import com.zone24x7.ibrac.recengine.configuration.sync.RecConfiguration;
import com.zone24x7.ibrac.recengine.configuration.sync.RuleConfiguration;
import com.zone24x7.ibrac.recengine.dao.DatasourceAdapter;
import com.zone24x7.ibrac.recengine.dao.HBaseAdapter;
import com.zone24x7.ibrac.recengine.exceptions.MalformedConfigurationException;
import com.zone24x7.ibrac.recengine.pojo.algoparams.AlgoParams;
import com.zone24x7.ibrac.recengine.strategy.FlatRecStrategy;
import com.zone24x7.ibrac.recengine.strategy.StrategyExecutor;
import com.zone24x7.ibrac.recengine.util.AppConfigStringConstants;
import com.zone24x7.ibrac.recengine.util.ConfigDataTransformUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Configuration
@EnableScheduling
public class SpringMainConfig {

    @Value(AppConfigStringConstants.CONFIG_RESOURCE_CLASSPATH_ALGO_PARAMS)
    private Resource algoParamsResourceFile;

    @Value(AppConfigStringConstants.CONFIG_SYNC_STRATEGY)
    private String configSyncStrategy;

    @Bean
    @Qualifier("cachedThreadPoolTaskExecutor")
    public ExecutorService getTaskExecutor() {
        return Executors.newCachedThreadPool();
    }

    @Bean
    @Qualifier("strategyExecutors")
    public StrategyExecutor provideStrategyExecutors(FlatRecStrategy flatRecStrategy) {
        flatRecStrategy.setNextExecutor(null);
        return flatRecStrategy;
    }

    @Bean
    public DatasourceAdapter getDataSource() {
        //TODO: if else according to data store initiate the bean
        return new HBaseAdapter();
    }

    @Bean
    @Qualifier("CsConfigurations")
    public Map<String, CsConfiguration> getCsConfigurations(RecConfiguration RecConfiguration, RuleConfiguration ruleConfiguration) {
        Map<String, CsConfiguration> csConfigurationHashMap = new HashMap<>();
        csConfigurationHashMap.put("bundleConfiguration", RecConfiguration);
        csConfigurationHashMap.put("ruleConfiguration", ruleConfiguration);
        return csConfigurationHashMap;
    }

    @Bean
    @Qualifier("ConfigSyncLock")
    public ReentrantReadWriteLock getConfigSyncLock() {
        return new ReentrantReadWriteLock();
    }

    @Bean
    public CsConfigurationsFetchStrategy getCsConfigurationsReadStrategy() throws MalformedConfigurationException {
        if ("api".equals(configSyncStrategy)) {
            return new CsConfigurationsFetchApiCallStrategy();
        } else if ("localconfig".equals(configSyncStrategy)) {
            return new CsConfigurationsFetchLocalConfigStrategy();
        }

        throw new MalformedConfigurationException("Unknown config syncup strategy in application.properties");
    }

    /**
     * Provider method for algo params map.
     *
     * @return algo params map.
     * @throws MalformedConfigurationException if configuration is malformed.
     */
    @Bean
    @Qualifier("algoParamsMap")
    public Map<String, AlgoParams> provideAlgoParamsMap() throws MalformedConfigurationException {
        String algoParamsConfig;

        try {
            algoParamsConfig = FileUtils.readFileToString(algoParamsResourceFile.getFile(), Charset.defaultCharset());
        } catch (IOException e) {
            throw new MalformedConfigurationException("Error reading algor params file.", e);
        }

        return ConfigDataTransformUtil.convertToAlgoParamsMap(algoParamsConfig);
    }
}