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
import org.apache.commons.lang3.StringUtils;
import com.zone24x7.ibrac.reconlib.api.ProductApi;
import com.zone24x7.ibrac.reconlib.api.ReconLibProductApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
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
    @Value(AppConfigStringConstants.CONFIG_RESOURCE_CLASSPATH_ALGO_PARAMS)
    private Resource algoParamsResourceFile;

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
     * @param flatRecStrategy the flat rec strategy to include.
     * @return the strategy executor chain
     */
    @Bean
    @Qualifier("strategyExecutors")
    public StrategyExecutor provideStrategyExecutors(FlatRecStrategy flatRecStrategy) {
        flatRecStrategy.setNextExecutor(null);
        return flatRecStrategy;
    }

    /**
     * Method to provide the data source adapter.
     *
     * @return the data source adapter
     */
    @Bean
    public DatasourceAdapter getDataSource() {
        //TODO: if else according to data store initiate the bean
        return new HBaseAdapter();
    }

    /**
     * Method to provide the configuration service related configurations map.
     *
     * @param RecConfiguration  the recommendation configurations
     * @param ruleConfiguration the rule configurations
     * @return the configurations map
     */
    @Bean
    @Qualifier("CsConfigurations")
    public Map<String, CsConfiguration> getCsConfigurations(RecConfiguration RecConfiguration, RuleConfiguration ruleConfiguration) {
        Map<String, CsConfiguration> csConfigurationHashMap = new HashMap<>();
        csConfigurationHashMap.put("bundleConfiguration", RecConfiguration);
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
     * Method to provide error properties instance of rec related error messages & codes.
     *
     * @return properties instance that contains rec related errors.
     * @throws IOException if an IO exception occurs.
     */
    @Bean
    @Qualifier("errorProperties")
    public Properties provideErrorProperties() throws IOException {
        Resource resource = new ClassPathResource("error.properties");
        return PropertiesLoaderUtils.loadProperties(resource);
    }

    /**
     * Method to provide ccp properties of rec engine.
     *
     * @return properties instance that contains rec ccp properties.
     * @throws IOException if an IO exception occurs.
     */
    @Bean
    @Qualifier("ccpProperties")
    public Properties provideCcpProperties() throws IOException {
        Resource resource = new ClassPathResource("ccp.properties");
        return PropertiesLoaderUtils.loadProperties(resource);
    }

    /**
     * Method to provide whitelisted ccp keys that is supported for rec generation.
     *
     * @param properties properties instance that contains rec ccp properties.
     * @return List of whitelisted ccp keys.
     */
    @Bean
    @Qualifier("whitelistedCcpKeys")
    public List<String> provideWhitelistedCapKeys(@Qualifier("ccpProperties") Properties properties) {
        String whiteListedCcpKeysString = properties.getProperty("whitelisted.ccp.keys");
        List<String> whiteListedCcpKeys = new LinkedList<>();

        if (StringUtils.isNotEmpty(whiteListedCcpKeysString)) {
            whiteListedCcpKeys.addAll(Arrays.asList(whiteListedCcpKeysString.split(",")));
        }
        return whiteListedCcpKeys;
    }

    /**
     * Provides the {@link ProductApi} instance.
     *
     * @return {@link ReconLibProductApi} instance.
     */
    @Bean
    @Qualifier("productApi")
    public ProductApi getReconLibProductApi() {
        return new ReconLibProductApi();
    }
}