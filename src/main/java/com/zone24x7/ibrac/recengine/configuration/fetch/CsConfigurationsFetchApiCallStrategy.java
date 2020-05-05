package com.zone24x7.ibrac.recengine.configuration.fetch;

import com.zone24x7.ibrac.recengine.util.AppConfigStringConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Calls the configuration apis and populated the temp cache
 * Note: Dependency injection binding through the config class to support dynamic injection based on selected strategy
 */
public class CsConfigurationsFetchApiCallStrategy implements CsConfigurationsFetchStrategy {

    @Autowired
    private CsConfigurationTempCache csConfigurationTempCache;

    @Value(AppConfigStringConstants.CONFIG_SYNC_API_REC_SLOT)
    private String recSlotUrl;
    @Value(AppConfigStringConstants.CONFIG_SYNC_API_REC)
    private String recUrl;
    @Value(AppConfigStringConstants.CONFIG_SYNC_API_BUNDLE)
    private String bundleUrl;
    @Value(AppConfigStringConstants.CONFIG_SYNC_API_RULE)
    private String ruleUrl;

    @Value(AppConfigStringConstants.CONFIG_SYNC_API_CALL_READ_TIMEOUT)
    private int readTimeout;

    @Value(AppConfigStringConstants.CONFIG_SYNC_API_CALL_CONNECTION_TIMEOUT)
    private int connectionTimeout;

    /**
     * Fetch configurations from given api calls
     *
     * @throws CsConfigurationFetchException when configurations fetching is failed
     */
    @Override
    public void fetchConfigurations() throws CsConfigurationFetchException {
        RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());

        try {
            String recResult = restTemplate.getForObject(recUrl, String.class);
            String recSlotResult = restTemplate.getForObject(recSlotUrl, String.class);
            String bundleResult = restTemplate.getForObject(bundleUrl, String.class);
            String ruleResult = restTemplate.getForObject(ruleUrl, String.class);
            csConfigurationTempCache.setConfigurations(recResult, recSlotResult, bundleResult, ruleResult);
        } catch (RestClientException e) {
            throw new CsConfigurationFetchException();
        }
    }

    /**
     * Create and returns the client HttpRequest factory with read and connection timeouts
     *
     * @return an instance of ClientHttpRequestFactory
     */
    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(connectionTimeout);
        clientHttpRequestFactory.setReadTimeout(readTimeout);
        return clientHttpRequestFactory;
    }
}