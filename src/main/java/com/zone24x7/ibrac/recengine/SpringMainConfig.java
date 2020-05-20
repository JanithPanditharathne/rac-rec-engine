package com.zone24x7.ibrac.recengine;

import com.zone24x7.ibrac.recengine.configuration.fetch.CsConfigurationsFetchApiCallStrategy;
import com.zone24x7.ibrac.recengine.configuration.fetch.CsConfigurationsFetchStrategy;
import com.zone24x7.ibrac.recengine.configuration.sync.RecConfiguration;
import com.zone24x7.ibrac.recengine.configuration.sync.CsConfiguration;
import com.zone24x7.ibrac.recengine.configuration.sync.RuleConfiguration;
import com.zone24x7.ibrac.recengine.converters.TableConfigJsonToTableConfigMapConverter;
import com.zone24x7.ibrac.recengine.dao.DatasourceAdapter;
import com.zone24x7.ibrac.recengine.dao.HBaseAdapter;
import com.zone24x7.ibrac.recengine.exceptions.MalformedConfigurationException;
import com.zone24x7.ibrac.recengine.pojo.tableconfigs.TableConfigInfo;
import com.zone24x7.ibrac.recengine.strategy.FlatRecStrategy;
import com.zone24x7.ibrac.recengine.strategy.StrategyExecutor;
import com.zone24x7.ibrac.recengine.util.AppConfigStringConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Configuration
@EnableScheduling
public class SpringMainConfig {

    @Autowired
    private TableConfigJsonToTableConfigMapConverter tableConfigJsonToTableConfigMapConverter;

    @Value(AppConfigStringConstants.TABLE_CONFIG_FILE_NAME)
    private Resource resource;

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
    @Qualifier("tableConfigurationMap")
    public Map<String, TableConfigInfo> loadTableConfigs() throws IOException, MalformedConfigurationException {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            String tableConfigString = FileCopyUtils.copyToString(reader);

            // Load table configs from json file and set the returning map to tableConfigReaderService for future use.
            return tableConfigJsonToTableConfigMapConverter.convert(tableConfigString);
        }
    }

    @Bean
    public CsConfigurationsFetchStrategy getCsConfigurationsReadStrategy() {
        return new CsConfigurationsFetchApiCallStrategy();
    }
}