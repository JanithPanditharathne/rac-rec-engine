package com.zone24x7.ibrac.recengine;

import com.zone24x7.ibrac.recengine.dao.DatasourceAdapter;
import com.zone24x7.ibrac.recengine.dao.HBaseAdapter;
import com.zone24x7.ibrac.recengine.strategy.FlatRecStrategy;
import com.zone24x7.ibrac.recengine.strategy.StrategyExecutor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

@Configuration
public class SpringMainConfig {

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
}
