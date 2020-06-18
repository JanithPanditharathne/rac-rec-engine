package com.zone24x7.ibrac.recengine;

import com.zone24x7.ibrac.recengine.converters.TableConfigJsonToTableConfigMapConverter;
import com.zone24x7.ibrac.recengine.exceptions.MalformedConfigurationException;
import com.zone24x7.ibrac.recengine.pojo.algoparams.AlgoParams;
import com.zone24x7.ibrac.recengine.pojo.tableconfigs.TableConfigInfo;
import com.zone24x7.ibrac.recengine.util.AppConfigStringConstants;
import com.zone24x7.ibrac.recengine.util.ConfigDataTransformUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Class to represent the external properties based configurations and bindings.
 */
@Configuration
public class ExternalPropertyBasedConfig {
    @Value(AppConfigStringConstants.CONFIG_RESOURCE_CLASSPATH_ALGO_PARAMS)
    private Resource algoParamsResourceFile;

    @Value(AppConfigStringConstants.TABLE_CONFIG_FILE_NAME)
    private Resource resource;

    @Autowired
    private TableConfigJsonToTableConfigMapConverter tableConfigJsonToTableConfigMapConverter;

    /**
     * Method to provide error properties instance of rec related error messages & codes.
     *
     * @return properties instance that contains rec related errors.
     * @throws IOException if an IO exception occurs.
     */
    @Bean
    @Qualifier("errorProperties")
    public Properties provideErrorProperties() throws IOException {
        Resource errorPropertyResource = new ClassPathResource("error.properties");
        return PropertiesLoaderUtils.loadProperties(errorPropertyResource);
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
        Resource ccpPropertyResource = new ClassPathResource("ccp.properties");
        return PropertiesLoaderUtils.loadProperties(ccpPropertyResource);
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
     * Provider method for algo params map.
     *
     * @return algo params map.
     * @throws MalformedConfigurationException if configuration is malformed.
     */
    @Bean
    @Qualifier("algoParamsMap")
    public Map<String, AlgoParams> provideAlgoParamsMap() throws MalformedConfigurationException {
        try (Reader reader = new InputStreamReader(algoParamsResourceFile.getInputStream(), StandardCharsets.UTF_8)) {
            String algoParamsConfig = FileCopyUtils.copyToString(reader);
            return ConfigDataTransformUtil.convertToAlgoParamsMap(algoParamsConfig);
        } catch (IOException e) {
            throw new MalformedConfigurationException("Error occurred when reading algorithm parameters file.", e);
        }
    }

    /**
     * Method to load the table configurations.
     *
     * @return the table configs map
     * @throws MalformedConfigurationException if an configuration error occurs
     */
    @Bean
    @Qualifier("tableConfigurationMap")
    public Map<String, TableConfigInfo> loadTableConfigs() throws MalformedConfigurationException {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            String tableConfigString = FileCopyUtils.copyToString(reader);

            // Load table configs from json file and set the returning map to tableConfigReaderService for future use.
            return tableConfigJsonToTableConfigMapConverter.convert(tableConfigString);
        } catch (IOException e) {
            throw new MalformedConfigurationException("Error occurred when reading table configs file.", e);
        }
    }
}