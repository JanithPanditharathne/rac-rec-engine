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
import com.zone24x7.ibrac.recengine.strategy.FlatRecStrategyExecutor;
import com.zone24x7.ibrac.recengine.strategy.StrategyExecutor;
import com.zone24x7.ibrac.recengine.strategy.UnknownStrategyExecutor;
import com.zone24x7.ibrac.recengine.util.AppConfigStringConstants;
import com.zone24x7.ibrac.reconlib.api.ProductApi;
import com.zone24x7.ibrac.reconlib.api.ReconLibProductApi;
import com.zone24x7.ibrac.reconlib.util.ApplicationConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Pattern;

/**
 * Class to represent the spring main configurations and bindings.
 */
@Configuration
@EnableScheduling
public class SpringMainConfig {
    @Value(AppConfigStringConstants.CONFIG_SYNC_STRATEGY)
    private String configSyncStrategy;

    /**
     * Method to provide the executor service.
     *
     * @return the executor service
     */
    @Bean
    @Qualifier("cachedThreadPoolTaskExecutor")
    public ExecutorService getTaskExecutor() {
        return Executors.newCachedThreadPool();
    }

    /**
     * Provider method for strategy executors chain
     *
     * @param flatRecStrategyExecutor the flat rec strategy to include.
     * @return the strategy executor chain
     */
    @Bean
    @Qualifier("strategyExecutors")
    public StrategyExecutor provideStrategyExecutors(FlatRecStrategyExecutor flatRecStrategyExecutor,
                                                     UnknownStrategyExecutor unknownStrategyExecutor) {
        flatRecStrategyExecutor.setNextExecutor(unknownStrategyExecutor);
        return flatRecStrategyExecutor;
    }

    /**
     * Method to provide the data source adapter.
     *
     * @return the data source adapter
     */
    @Bean
    public DatasourceAdapter getDataSource() {
        // NOTE : if the data source changes, provide the proper data source according to a conditional config.
        return new HBaseAdapter();
    }

    /**
     * Method to provide the configuration service related configurations map.
     *
     * @param recConfiguration  the recommendation configurations
     * @param ruleConfiguration the rule configurations
     * @return the configurations map
     */
    @Bean
    @Qualifier("CsConfigurations")
    public Map<String, CsConfiguration> getCsConfigurations(RecConfiguration recConfiguration, RuleConfiguration ruleConfiguration) {
        Map<String, CsConfiguration> csConfigurationHashMap = new HashMap<>();
        csConfigurationHashMap.put("bundleConfiguration", recConfiguration);
        csConfigurationHashMap.put("ruleConfiguration", ruleConfiguration);
        return csConfigurationHashMap;
    }

    /**
     * Method to provide the configuration sync lock.
     *
     * @return the configuration sync lock
     */
    @Bean
    @Qualifier("ConfigSyncLock")
    public ReentrantReadWriteLock getConfigSyncLock() {
        return new ReentrantReadWriteLock();
    }

    /**
     * Method to provide the configuration fetch strategy.
     *
     * @return the configuration fetch strategy
     * @throws MalformedConfigurationException if the configuration is malformed
     */
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
     * Method to provide input validation pattern.
     *
     * @param regex input validation pattern as a string.
     * @return input validation regex.
     */
    @Bean
    @Qualifier("inputParamValidationPattern")
    public Pattern provideInputParameterValidationPattern(@Value("${input.param.whitelisted.regex}") String regex) {
        return Pattern.compile(regex);
    }

    /**
     * Method to provide input validation pattern with extra supported chars.
     *
     * @param regex input validation pattern as a string.
     * @return input validation regex.
     */
    @Bean
    @Qualifier("inputParamValidationPatternExtraChars")
    public Pattern provideInputParameterValidationPatternWithExtraChars(@Value("${input.param.whitelisted.extra.chars.regex}") String regex) {
        return Pattern.compile(regex);
    }

    /**
     * Provides the {@link ProductApi} instance.
     *
     * @return {@link ReconLibProductApi} instance.
     */
    @Bean
    @Qualifier("productApi")
    public ProductApi getReconLibProductApi(ReconLibConfig reconLibConfig) {
        ApplicationConfig.getInstance().initializePropertiesConfiguration(reconLibConfig.getConfigurations());
        return new ReconLibProductApi();
    }

    /**
     * Method to get the timezone id of the recengine
     *
     * @param timezoneName configured timezone name
     * @return Zone id
     */
    @Bean
    @Qualifier("timeZoneId")
    public ZoneId getTimeZoneId(@Value("${recengine.timezone}") String timezoneName) {
        return ZoneId.of(timezoneName);
    }
}