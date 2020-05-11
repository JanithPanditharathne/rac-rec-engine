package com.zone24x7.ibrac.recengine.configuration.fetch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Test class of CsConfigurationsFetchApiCallStrategy
 */
class CsConfigurationsFetchApiCallStrategyTest {

    private CsConfigurationsFetchApiCallStrategy csConfigurationsFetchStrategy = new CsConfigurationsFetchApiCallStrategy();
    private CsConfigurationTempCache csConfigurationTempCache;
    private static final String REC_SLOT_URL = "recSlotUrl";
    private static final String REC_URL = "recUrl";
    private static final String BUNDLE_URL = "bundleUrl";
    private static final String RULE_URL = "ruleUrl";

    private static final String REC_SLOT_CONFIG = "{\"recSlots\":[]}";
    private static final String REC_CONFIG = "{\"recs\":[]}";
    private static final String BUNDLE_CONFIG = "{\"bundles\":[]}";
    private static final String RULE_CONFIG = "{\"rules\":[]}";

    private static final Integer READ_TIMEOUT = 100;
    private static final Integer CONNECT_TIMEOUT = 200;
    private RestTemplateBuilder restTemplateBuilder;
    private RestTemplate restTemplate;

    /**
     * Things to run before each test
     */
    @BeforeEach
    void setUp() {
        restTemplateBuilder = mock(RestTemplateBuilder.class);
        csConfigurationTempCache = mock(CsConfigurationTempCache.class);
        restTemplate = mock(RestTemplate.class);

        ReflectionTestUtils.setField(csConfigurationsFetchStrategy, "csConfigurationTempCache", csConfigurationTempCache);
        ReflectionTestUtils.setField(csConfigurationsFetchStrategy, "recSlotUrl", REC_SLOT_URL);
        ReflectionTestUtils.setField(csConfigurationsFetchStrategy, "recUrl", REC_URL);
        ReflectionTestUtils.setField(csConfigurationsFetchStrategy, "bundleUrl", BUNDLE_URL);
        ReflectionTestUtils.setField(csConfigurationsFetchStrategy, "ruleUrl", RULE_URL);

        ReflectionTestUtils.setField(csConfigurationsFetchStrategy, "readTimeout", READ_TIMEOUT);
        ReflectionTestUtils.setField(csConfigurationsFetchStrategy, "connectionTimeout", CONNECT_TIMEOUT);

        ReflectionTestUtils.setField(csConfigurationsFetchStrategy, "restTemplateBuilder", restTemplateBuilder);

        when(restTemplateBuilder.setConnectTimeout(Duration.ofMillis(CONNECT_TIMEOUT))).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.setReadTimeout(Duration.ofMillis(READ_TIMEOUT))).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);

        when(restTemplate.getForObject(REC_SLOT_URL, String.class)).thenReturn(REC_SLOT_CONFIG);
        when(restTemplate.getForObject(REC_URL, String.class)).thenReturn(REC_CONFIG);
        when(restTemplate.getForObject(BUNDLE_URL, String.class)).thenReturn(BUNDLE_CONFIG);
        when(restTemplate.getForObject(RULE_URL, String.class)).thenReturn(RULE_CONFIG);
    }

    /**
     * Fetch configurations happy path
     */
    @Test
    void should_fetch_configuration() throws CsConfigurationFetchException {
        csConfigurationsFetchStrategy.fetchConfigurations();
        verify(csConfigurationTempCache, times(1)).setConfigurations(REC_SLOT_CONFIG, REC_CONFIG, BUNDLE_CONFIG, RULE_CONFIG);
        verify(restTemplate).getForObject(REC_SLOT_URL, String.class);
        verify(restTemplate).getForObject(REC_URL, String.class);
        verify(restTemplate).getForObject(BUNDLE_URL, String.class);
        verify(restTemplate).getForObject(RULE_URL, String.class);
    }

    /**
     * Fetch configurations throws an CsConfigurationFetchException
     */
    @Test
    void should_throw_CsConfigurationFetchException_if_exception_occurs() {
        when(restTemplate.getForObject(REC_SLOT_URL, String.class)).thenThrow(new RestClientException(""));

        assertThrows(CsConfigurationFetchException.class, () -> {
            csConfigurationsFetchStrategy.fetchConfigurations();
        });

        verify(csConfigurationTempCache, never()).setConfigurations(REC_SLOT_CONFIG, REC_CONFIG, BUNDLE_CONFIG, RULE_CONFIG);
    }
}